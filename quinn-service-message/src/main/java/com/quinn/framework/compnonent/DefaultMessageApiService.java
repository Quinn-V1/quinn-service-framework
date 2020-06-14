package com.quinn.framework.compnonent;

import com.quinn.framework.api.message.MessageInstance;
import com.quinn.framework.api.message.MessageSendRecord;
import com.quinn.framework.api.message.MessageTemp;
import com.quinn.framework.model.DirectMessageInfo;
import com.quinn.framework.model.MessageSendParam;
import com.quinn.framework.service.MessageApiService;
import com.quinn.framework.service.MessageHelpService;
import com.quinn.framework.service.MessageSendService;
import com.quinn.framework.util.MessageInfoUtil;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.model.BaseResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    @Resource
    private MessageHelpService messageHelpService;

    /**
     * 消息模板业务操作接口
     */
    @Resource
    private MessageSendService messageSendService;

    /**
     * 消息线程池执行器
     */
    @Resource
    @Qualifier("messageExecutorService")
    private ExecutorService messageExecutorService;

    @Override
    public BaseResult doSend(MessageSendParam messageSendParam) {
        // 参数内部校验
        BaseResult validate = messageSendParam.validate();
        if (!validate.isSuccess()) {
            return BaseResult.fromPrev(validate);
        }

        // 生成可直接发送的对象
        BaseResult<DirectMessageInfo> directMessageInfoRes = generateInfo(messageSendParam);
        if (!directMessageInfoRes.isSuccess()) {
            return BaseResult.fromPrev(directMessageInfoRes);
        }

        // 循环保存消息实例和消息发送记录
        DirectMessageInfo directMessageInfo = directMessageInfoRes.getData();
        Map<String, List<MessageSendRecord>> sendRecordListMap = directMessageInfo.getSendRecordListMap();
        BaseResult result = new BaseResult<>();

        Set<String> sendRecordKeys = new HashSet<>();
        for (Map.Entry<String, List<MessageSendRecord>> entry : sendRecordListMap.entrySet()) {
            List<MessageSendRecord> sendRecordList = entry.getValue();
            String key = entry.getKey();

            // 保存消息实例
            MessageInstance instance = directMessageInfo.getInstance(key);
            messageHelpService.saveInstance(instance);

            if (instance != null) {
                // 保存消息发送记录
                for (MessageSendRecord sendRecord : sendRecordList) {
                    MessageInfoUtil.fillSendRecordWithInstance(sendRecord, instance);

                    String dataKey = sendRecord.dataKey();
                    if (sendRecordKeys.contains(dataKey)) {
                        continue;
                    }
                    sendRecordKeys.add(dataKey);
                    messageHelpService.saveSendRecord(sendRecord);
                }

                // 发送消息
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

    @Override
    public BaseResult revokeBySendIds(Long[] sendRecordIds) {
        return null;
    }

    @Override
    public BaseResult revokeByInstIds(Long[] instIds) {
        return null;
    }

    @Override
    public BaseResult revokeByBathNo(String batchKey) {
        return null;
    }

    /**
     * 生成消息信息
     *
     * @param messageSendParam 发送参数
     * @return 生成成功
     */
    private BaseResult<DirectMessageInfo> generateInfo(MessageSendParam messageSendParam) {
        BaseResult repeatRes = validateRepeat(messageSendParam);
        if (!repeatRes.isSuccess()) {
            return BaseResult.fromPrev(repeatRes);
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

        // 业务主键可能是占位参数
        if (messageSendParam.getMessageParam() != null) {
            fromSystem = FreeMarkTemplateLoader.invoke(fromSystem, messageSendParam.getMessageParam());
            businessKey = FreeMarkTemplateLoader.invoke(businessKey, messageSendParam.getMessageParam());
            messageSendParam.setBusinessKey(businessKey);
            messageSendParam.setFromSystem(fromSystem);
        }

        // 业务主键不可重复
        BaseResult<MessageInstance> select = messageHelpService.getInstanceByBiz(fromSystem, businessKey);
        if (select.isSuccess()) {
            // FIXME
            return BaseResult.fail("业务主键重复").ofData(select.getData());
        }

        return BaseResult.SUCCESS;
    }

    /**
     * 没有指定模板编码胡情况下校验参数
     *
     * @param messageSendParam 消息发送参数
     * @return 校验成功则为空，失败则为失败消息
     */
    private BaseResult<DirectMessageInfo> validateWithoutTempKey(MessageSendParam messageSendParam) {
        DirectMessageInfo directMessageInfo = DirectMessageInfo.newInstance();

        // 模板变成可直接发送的消息（视情况添加线程-准备做）
        tempToDirect(messageSendParam, directMessageInfo);

        // 模板变成可直接发送的消息
        directMessageInfo.initDirect(messageSendParam);

        // 通过多线程执行解析
        directMessageInfo.executeBy(messageExecutorService);

        return BaseResult.success(directMessageInfo);
    }

    /**
     * 指定模板ID的情况下校验参数
     *
     * @param messageSendParam 发送参数
     * @return 校验结果
     */
    private BaseResult<DirectMessageInfo> validateWithTempKey(MessageSendParam messageSendParam) {
        String templateKey = messageSendParam.getTemplateKey();

        // 查找模板主数据：是否查出内容更具体
        BaseResult<MessageTemp> result = messageHelpService.getTemplateByKey(templateKey);
        if (!result.isSuccess()) {
            return BaseResult.fail(result.getMessage());
        }

        // 如果参数没有指定地址，则需要指定默认地址
        MessageTemp messageTemp = result.getData();
        if (!messageTemp.hasDefaultAddress() && CollectionUtils.isEmpty(messageSendParam.getReceivers())) {
            // FIXME
            return BaseResult.fail("未指定消息发送地址");
        }

        MessageInfoUtil.fillMessageSendParamWithTemp(messageSendParam, messageTemp);
        DirectMessageInfo directMessageInfo = DirectMessageInfo.newInstance();
        tempToDirect(messageSendParam, directMessageInfo);

        if (StringUtils.isEmpty(messageSendParam.getContent())) {
            // 解析消息内容（可能依赖参数信息、收件信息）
            ContentToDirect contentToDirect = new ContentToDirect(directMessageInfo, messageSendParam,
                    messageHelpService);
            directMessageInfo.addThread(contentToDirect);
        } else {
            directMessageInfo.initDirect(messageSendParam);
        }

        directMessageInfo.executeBy(messageExecutorService);
        return BaseResult.success(directMessageInfo);
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
        ParameterToDirect parameterToDirect;
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
