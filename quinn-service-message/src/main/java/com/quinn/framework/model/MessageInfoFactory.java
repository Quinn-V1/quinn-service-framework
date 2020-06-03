package com.quinn.framework.model;

import com.quinn.framework.api.message.MessageInfoSupplier;
import com.quinn.framework.api.message.MessageInstance;
import com.quinn.framework.api.message.MessageTempContent;
import com.quinn.framework.util.enums.PlaceTypeEnum;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.StringConstant;
import org.springframework.util.StringUtils;

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
     * 创建消息实例
     *
     * @param content          模板内容
     * @param messageSendParam 消息参数
     * @return 消息实例
     */
    public static MessageInstance createInstance(MessageTempContent content, MessageSendParam messageSendParam) {
        MessageInstance instance = messageInfoSupplier.createInstance();
        instance.setMessageType(content.getMessageType());
        instance.setLangCode(content.getLanguageCode());

        instance.setSubject(content.getSubjectTemplate());
        instance.setMsgUrl(content.getUrlTemplate());
        instance.setAttachment(content.getAttachmentTemplate());
        instance.setContent(content.getContentTemplate());

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
        instance.setMsgUrl(messageSendParam.getUrl());
        instance.setAttachment(messageSendParam.getAttachment());
        instance.setContent(content);

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

        return instance;
    }

    /**
     * 设置消息信息提供者
     *
     * @param messageInfoSupplier 消息信息提供者
     */
    public static void setMessageInfoSupplier(MessageInfoSupplier messageInfoSupplier) {
        MessageInfoFactory.messageInfoSupplier = messageInfoSupplier;
    }
}
