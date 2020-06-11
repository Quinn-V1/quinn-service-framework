package com.quinn.framework.model;

import com.quinn.framework.api.message.*;
import com.quinn.framework.util.enums.PlaceTypeEnum;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.BaseUtil;
import com.quinn.util.base.NumberUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.CharConstant;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.UrgentLevelEnum;
//import com.quinn.util.licence.model.ApplicationInfo;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.quinn.framework.model.DirectMessageInfo.MAX_SUBJECT_LENGTH;

/**
 * 消息信息工厂
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public class MessageInfoFactory {

    /**
     * 消息信息提供者
     */
    private static MessageInfoSupplier messageInfoSupplier;

    /**
     * 小弟地址解析器
     */
    private static Map<String, MessageAddressResolver> addressResolverMap;

    /**
     * 创建消息实例
     *
     * @param content          模板内容
     * @param messageSendParam 消息参数
     * @return 消息实例
     */
    public static MessageInstance createInstance(MessageTempContent content, MessageSendParam messageSendParam) {
        MessageInstance instance = messageInfoSupplier.createInstance();
        instance.setMessageType(content.getMessageType());
        instance.setLangCode(content.getLangCode());

        instance.setSubject(content.getSubjectTemplate());
        instance.setMsgUrl(content.getUrlTemplate());
        instance.setAttachment(content.getAttachmentTemplate());
        instance.setContent(content.getContentTemplate());

        instance.setTemplateId(content.getTemplateId());
        instance.setTemplateKey(content.getTemplateKey());

        // 替换方案 , String fromSystem, String businessKey
        Integer placeType = content.getPlaceTypes();
        if (placeType != null) {
            Map<String, Object> messageParam = messageSendParam.getMessageParam();
            if ((PlaceTypeEnum.SUBJECT.code & placeType) > 0) {
                instance.setSubject(FreeMarkTemplateLoader.invoke(instance.getSubject(), messageParam));
            }
            if ((PlaceTypeEnum.URL.code & placeType) > 0) {
                instance.setMsgUrl(FreeMarkTemplateLoader.invoke(instance.getMsgUrl(), messageParam));
            }
            if ((PlaceTypeEnum.ATTACHMENT.code & placeType) > 0) {
                instance.setAttachment(FreeMarkTemplateLoader.invoke(instance.getAttachment(), messageParam));
            }
            if ((PlaceTypeEnum.CONTENT.code & placeType) > 0) {
                instance.setContent(FreeMarkTemplateLoader.invoke(instance.getContent(), messageParam));
            }
        }

        instance.setFromSystem(messageSendParam.getFromSystem());
        instance.setBusinessKey(messageSendParam.getBusinessKey());
        instance.setSender(messageSendParam.senderOfSave());

        Integer urgentLevel = messageSendParam.getUrgentLevel();
        if (NumberUtil.isEmptyInFrame(urgentLevel)) {
            urgentLevel = UrgentLevelEnum.NORMAL.code;
        }
        instance.setUrgentLevel(urgentLevel);
        return instance;
    }

    /**
     * 创建消息实例 - 这种情况就不依赖模板了
     *
     * @param messageSendParam 消息内容
     * @return 消息实例
     */
    public static MessageInstance createInstance(MessageSendParam messageSendParam) {
        MessageInstance instance = messageInfoSupplier.createInstance();

        String content = messageSendParam.getContent();
        String subject = messageSendParam.getSubject();

        if (StringUtil.isEmpty(content)) {
            content = subject;
        } else if (StringUtil.isEmpty(subject)) {
            instance.setSubject(StringUtil.omitByLength(content, MAX_SUBJECT_LENGTH));
        }

        instance.setSubject(subject);
        instance.setMsgUrl(BaseUtil.ifNull(messageSendParam.getUrl(), StringConstant.NONE_OF_DATA));
        instance.setAttachment(BaseUtil.ifNull(messageSendParam.getAttachment(), StringConstant.NONE_OF_DATA));
        instance.setContent(content);

        instance.setTemplateId(NumberConstant.NONE_OF_DATA_ID);
        instance.setTemplateKey(StringConstant.NONE_OF_DATA);

        instance.setFromSystem(messageSendParam.getFromSystem());
        instance.setBusinessKey(messageSendParam.getBusinessKey());
        instance.setSender(messageSendParam.senderOfSave());

        if (!StringUtils.isEmpty(messageSendParam.getLangCode())) {
            instance.setLangCode(messageSendParam.getLangCode());
        } else {
            instance.setLangCode(StringConstant.ALL_OF_DATA);
        }

        // 消息类型可以为空，因为可以根据消息发送对象计息出来
        if (!StringUtils.isEmpty(messageSendParam.getMessageType())) {
            instance.setMessageType(messageSendParam.getMessageType());
        } else {
            instance.setMessageType(StringConstant.ALL_OF_DATA);
        }

        Integer urgentLevel = messageSendParam.getUrgentLevel();
        if (NumberUtil.isEmptyInFrame(urgentLevel)) {
            urgentLevel = UrgentLevelEnum.NORMAL.code;
        }
        instance.setUrgentLevel(urgentLevel);

        return instance;
    }

    /**
     * 收件对象转化为消息发送记录
     *
     * @param receiver     收件对象
     * @param messageParam 消息发送记录
     * @return 消息发送记录
     */
    public static BaseResult<List<MessageSendRecord>> receiver2SendRecord(
            MessageReceiver receiver, Map<String, Object> messageParam) {

        String receiverType = receiver.getReceiverType();
        String receiverValue = receiver.getReceiverValue();
        String messageType = receiver.getMessageType();

        if (MsgReceiverTypeEnum.PARAM.name().equals(receiverType)) {
            receiverValue = FreeMarkTemplateLoader.invoke(receiverValue, messageParam);
            if (StringUtils.isEmpty(receiverValue)) {
                return BaseResult.fail("未解析到收件对象");
            }

            String realReceiverType = (String) messageParam.get("receiverType");
            if (!StringUtils.isEmpty(realReceiverType)) {
                realReceiverType = FreeMarkTemplateLoader.invoke(realReceiverType, messageParam);
            }

            if (!StringUtils.isEmpty(realReceiverType)) {
                receiverType = realReceiverType;
            } else {
                receiverType = MsgReceiverTypeEnum.DIRECT.name();
            }

            receiver.setReceiverValue(receiverValue);
            receiver.setReceiverType(receiverType);
        }

        String messageAddressResolverKey = receiverType.toUpperCase() + CharConstant.UNDERLINE
                + messageType.toUpperCase();
        MessageAddressResolver messageAddressResolver = addressResolverMap.get(MessageAddressResolver.BEAN_NAME_PREFIX
                + CharConstant.UNDERLINE + messageAddressResolverKey);

        if (messageAddressResolver == null) {
            // FIXME
            return BaseResult.fail("没有配置地址解析器" + messageAddressResolverKey);
        }

        BaseResult<List<MessageSendRecord>> result =
                messageAddressResolver.resolve(receiverValue, messageParam);

        if (result.isSuccess()) {
            List<MessageSendRecord> data = result.getData();

            String langCode = receiver.getLangCode();
            for (MessageSendRecord record : data) {
                record.setMessageType(receiver.getMessageType());
                if (StringUtil.isNotEmpty(langCode)) {
                    record.setLangCode(langCode);
                }

//                if (ApplicationInfo.getInstance().isSupportLang(record.getLangCode())) {
//                    record.setLangCode(receiver.getLangCode());
//                } else {
//                    record.setLangCode(ApplicationInfo.getInstance().getDefaultLangCode());
//                }

                if (!StringUtil.isEmptyInFrame(receiver.getServerKey())) {
                    record.setServerKey(receiver.getServerKey());
                }

                Integer urgentLevel = receiver.getUrgentLevel();
                if (NumberUtil.isEmptyInFrame(urgentLevel)) {
                    urgentLevel = UrgentLevelEnum.NORMAL.code;
                }
                record.setUrgentLevel(urgentLevel);
            }
        }

        return result;
    }


    /**
     * 设置消息信息提供者
     *
     * @param messageInfoSupplier 消息信息提供者
     */
    public static void setMessageInfoSupplier(MessageInfoSupplier messageInfoSupplier) {
        MessageInfoFactory.messageInfoSupplier = messageInfoSupplier;
    }

    /**
     * 设置地址解析对象Map
     *
     * @param addressResolverMap 地址解析对象Map
     */
    public static void setAddressResolverMap(Map<String, MessageAddressResolver> addressResolverMap) {
        MessageInfoFactory.addressResolverMap = addressResolverMap;
    }
}
