package com.quinn.framework.compnonent;

import com.quinn.framework.api.message.*;
import com.quinn.framework.model.DirectMessageInfo;
import com.quinn.framework.model.MessageSendParam;
import com.quinn.framework.util.MessageInfoUtil;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.model.BaseResult;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 消息发送默认实现类
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public class DefaultMessageApiService implements MessageApiService {

    /**
     * 消息模板业务操作接口
     */
    private MessageHelpService messageHelpService;

    /**
     * 消息模板业务操作接口
     */
    private MessageSendService messageSendService;

    /**
     * 消息线程池执行器
     */
    private ExecutorService messageExecutorService;

    @Override
    public BaseResult send(MessageSendParam messageSendParam) {
        BaseResult validate = messageSendParam.validate();
        if (!validate.isSuccess()) {
            return BaseResult.fromPrev(validate);
        }


        BaseResult<DirectMessageInfo> directMessageInfoRes = generateInfo(messageSendParam);
        if (!directMessageInfoRes.isSuccess()) {
            return BaseResult.fromPrev(directMessageInfoRes);
        }

        DirectMessageInfo directMessageInfo = directMessageInfoRes.getData();

        // 循环保存消息实例和消息发送记录
        Map<String, List<MessageSendRecord>> sendRecordListMap = directMessageInfo.getSendRecordListMap();
        BaseResult result = new BaseResult<>();

        for (Map.Entry<String, List<MessageSendRecord>> entry : sendRecordListMap.entrySet()) {
            List<MessageSendRecord> sendRecordList = entry.getValue();
            String key = entry.getKey();

            MessageInstance instance = directMessageInfo.getInstance(key);
            messageHelpService.saveInstance(instance);

            if (instance != null) {
                for (MessageSendRecord sendRecord : sendRecordList) {
                    MessageInfoUtil.fillSendRecordWithInstance(sendRecord, instance);
                    messageHelpService.saveSendRecord(sendRecord);
                }

                BaseResult res = messageSendService.sendAll(sendRecordList);
                if (!res.isSuccess()) {
                    result.appendMessage(res.getMessage());
                }
            }
        }

        return result;
    }

    @Override
    public BaseResult preview(MessageSendParam messageSendParam) {
        return null;
    }

    /**
     * 生成消息信息
     *
     * @param messageSendParam 发送参数
     * @return 生成成功
     */
    private BaseResult<DirectMessageInfo> generateInfo(MessageSendParam messageSendParam) {
        BaseResult rv = validateRepeat(messageSendParam);
        if (!rv.isSuccess()) {
            return BaseResult.fromPrev(rv);
        }

        String templateKey = messageSendParam.getTemplateKey();
        BaseResult<DirectMessageInfo> validateResult;
        if (StringUtil.isEmptyInFrame(templateKey)) {
            validateResult = validateWithoutTempKey(messageSendParam);
        } else {
            validateResult = validateWithTempKey(messageSendParam);
        }

        if (!validateResult.isSuccess()) {
            return BaseResult.fail(validateResult.getMessage());
        }

        DirectMessageInfo directMessageInfo = validateResult.getData();
        return directMessageInfo.ready();
    }

    /**
     * 校验是否重复
     *
     * @param messageSendParam 来源系统
     * @return 校验结果
     */
    public BaseResult validateRepeat(MessageSendParam messageSendParam) {
        String fromSystem = messageSendParam.getFromSystem();
        String businessKey = messageSendParam.getBusinessKey();

        if (StringUtils.isEmpty(fromSystem) || StringUtils.isEmpty(businessKey)) {
            // FIXME
            return BaseResult.fail("业务主键缺失，请指定参数fromSystem 和 businessKey");
        }

        if (messageSendParam.getMessageParam() != null) {
            fromSystem = FreeMarkTemplateLoader.invoke(fromSystem, messageSendParam.getMessageParam());
            businessKey = FreeMarkTemplateLoader.invoke(businessKey, messageSendParam.getMessageParam());
            messageSendParam.setBusinessKey(businessKey);
            messageSendParam.setFromSystem(fromSystem);
        }

        BaseResult<MessageInstance> select = messageHelpService.getInstanceByBiz(fromSystem, businessKey);

        if (select.isSuccess()) {
            // FIXME
            return BaseResult.fail("业务主键重复").ofData(select.getData());
        }

        return new BaseResult();
    }

    /**
     * 没有指定模板编码胡情况下校验参数
     *
     * @param messageSendParam 消息发送参数
     * @return 校验成功则为空，失败则为失败消息
     */
    private BaseResult<DirectMessageInfo> validateWithoutTempKey(MessageSendParam messageSendParam) {
        StringBuilder query = new StringBuilder();
        if (StringUtils.isEmpty(messageSendParam.getContent())) {
            query.append(" 消息内容不可为空");
        }

        if (CollectionUtils.isEmpty(messageSendParam.getReceivers())) {
            query.append(" 收件对象详细不可为空");
        }

        if (query.length() > 0) {
            query.insert(0, "消息模板为空的情况下");
            return BaseResult.fail(query.toString());
        }

        DirectMessageInfo messageInfo = DirectMessageInfo.newInstance();
        tempToDirect(messageSendParam, messageInfo);

        messageInfo.initDirect(messageSendParam);

        messageInfo.executeBy(messageExecutorService);
        return BaseResult.success(messageInfo);
    }

    /**
     * 指定模板ID的情况下校验参数
     *
     * @param messageSendParam 发送参数
     * @return 校验结果
     */
    private BaseResult<DirectMessageInfo> validateWithTempKey(MessageSendParam messageSendParam) {

        String templateKey = messageSendParam.getTemplateKey();
        BaseResult<MessageTemp> result = messageHelpService.getTemplateByKey(templateKey);
        if (!result.isSuccess()) {
            return BaseResult.fail(result.getMessage());
        }

        MessageTemp messageTemp = result.getData();
        if (!messageTemp.hasDefaultAddress() && CollectionUtils.isEmpty(messageSendParam.getReceivers())) {
            return BaseResult.fail("未指定消息发送地址");
        }

        DirectMessageInfo messageInfo = DirectMessageInfo.newInstance();
        tempToDirect(messageSendParam, messageInfo);

        if (StringUtils.isEmpty(messageSendParam.getContent())) {
            // 解析消息内容（可能依赖参数信息、收件信息）
            ContentToDirect contentToDirect = new ContentToDirect(messageInfo, messageSendParam, messageHelpService);
            messageInfo.addThread(contentToDirect);
        } else {
            messageInfo.initDirect(messageSendParam);
        }

        messageInfo.executeBy(messageExecutorService);
        return BaseResult.success(messageInfo);
    }

    /**
     * 将模板解析未直接内容（有无消息模板逻辑一样）
     *
     * @param messageSendParam 原始参数
     * @param messageInfo      直接内容
     */
    private void tempToDirect(MessageSendParam messageSendParam, DirectMessageInfo messageInfo) {
        // 解析参数信息
        Map<String, Object> messageParam = messageSendParam.getMessageParam();
        ParameterToDirect parameterToDirect = null;
        if (!CollectionUtils.isEmpty(messageParam)) {
            parameterToDirect = new ParameterToDirect(messageInfo, messageSendParam, messageHelpService);
            messageInfo.addThread(parameterToDirect);
        }

        // 解析收件信息（可能依赖参数信息）
        ReceiverToDirect receiverToDirect =
                new ReceiverToDirect(messageInfo, messageSendParam, messageHelpService);
        messageInfo.addThread(receiverToDirect);
    }
}
