package com.quinn.framework.util;

import com.quinn.framework.api.message.MessageInstance;
import com.quinn.framework.api.message.MessageSendRecord;
import com.quinn.framework.api.message.MessageTemp;
import com.quinn.framework.model.MessageReceiverAdapter;
import com.quinn.framework.model.MessageSendParam;
import com.quinn.util.base.NumberUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.StringConstant;
import org.springframework.cglib.core.Local;

import java.util.List;
import java.util.Locale;

/**
 * 消息对象操作工具类
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public final class MessageInfoUtil {

    private MessageInfoUtil() {
    }

    /**
     * 适用消息实例填充消息发送记录
     *
     * @param sendRecord 消息发送记录
     * @param instance   消息发送记录
     */
    public static void fillSendRecordWithInstance(MessageSendRecord sendRecord, MessageInstance instance) {
        sendRecord.setMessageInstance(instance);
        sendRecord.setMsgInstanceId(instance.getId());
        sendRecord.setBatchKey(instance.getBatchKey());
    }

    /**
     * 获取分组编码
     *
     * @param messageType 消息类型
     * @param langCode    语言编码
     * @return 发送分组
     */
    public static String sendGroup(String messageType, String langCode) {
        messageType = StringUtil.isEmptyInFrame(messageType) ? StringConstant.ALL_OF_DATA : messageType;
        langCode = StringUtil.isEmptyInFrame(langCode) ? Locale.getDefault().toString() : langCode;
        return messageType + StringConstant.CHAR_COLON + langCode;
    }

    /**
     * 拆分消息发送分组
     *
     * @param sendGroup 消息发送分组
     * @return 数组[消息类型, 语言编码]
     */
    public static final String[] splitSendGroup(String sendGroup) {
        return sendGroup.split(StringConstant.CHAR_COLON);
    }

    /**
     * 默认的次级主键
     *
     * @param messageType 消息类型
     * @param tenantCode  消息类型
     * @return 次级主键（参照MessageServer）
     */
    public static final String serverSubKey(String messageType, String tenantCode) {
        return messageType + StringConstant.CHAR_COLON + tenantCode;
    }

    /**
     * 默认的次级主键
     *
     * @param messageType 消息类型
     * @return 次级主键（参照MessageServer）
     */
    public static final String defaultServerSubKey(String messageType) {
        return messageType + StringConstant.CHAR_COLON + StringConstant.ALL_OF_DATA;
    }

    /**
     * 拆分消息服务分组
     *
     * @param serverSubKey 消息服务分组
     * @return 数组[消息类型, 租户编码]
     */
    public static final String[] splitServerSubKey(String serverSubKey) {
        return serverSubKey.split(StringConstant.CHAR_COLON);
    }

    /**
     * 使用模板填充消息发送参数
     *
     * @param messageSendParam 消息发送参数
     * @param messageTemp      消息模板
     */
    public static void fillMessageSendParamWithTemp(MessageSendParam messageSendParam, MessageTemp messageTemp) {
        messageSendParam.setTemplateId(messageTemp.getId());
        messageSendParam.setTemplateKey(messageTemp.getTemplateKey());

        if (NumberUtil.isEmptyInFrame(messageSendParam.getUrgentLevel())) {
            messageSendParam.setUrgentLevel(messageTemp.getUrgentLevel());
            List<MessageReceiverAdapter> receivers = messageSendParam.getReceivers();
            if (receivers != null) {
                for (MessageReceiverAdapter receiver : receivers) {
                    receiver.setUrgentLevel(messageTemp.getUrgentLevel());
                }
            }
        }
    }
}
