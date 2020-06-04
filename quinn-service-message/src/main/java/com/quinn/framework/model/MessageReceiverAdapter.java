package com.quinn.framework.model;

import com.quinn.framework.api.message.MessageReceiver;
import com.quinn.framework.util.MessageInfoUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 默认消息接受者
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
@Setter
@Getter
public class MessageReceiverAdapter implements MessageReceiver {

    public MessageReceiverAdapter() {
    }

    public static MessageReceiverAdapter from(MessageReceiver messageReceiver) {
        if (messageReceiver instanceof MessageReceiverAdapter) {
            return (MessageReceiverAdapter) messageReceiver;
        } else {
            MessageReceiverAdapter receiverAdapter = new MessageReceiverAdapter();
            receiverAdapter.setMessageType(messageReceiver.getMessageType());
            receiverAdapter.setLangCode(messageReceiver.getLangCode());
            receiverAdapter.setServerKey(messageReceiver.getServerKey());
            receiverAdapter.setReceiverType(messageReceiver.getReceiverType());
            receiverAdapter.setReceiverValue(messageReceiver.getReceiverValue());
            receiverAdapter.setUrgentLevel(messageReceiver.getUrgentLevel());
            return receiverAdapter;
        }
    }

    /**
     * 消息类型
     * <p>
     * length 20 0
     * nullable false
     */
    @ApiModelProperty("消息类型")
    private String messageType;

    /**
     * 语言编码
     * <p>
     * length 20 0
     * nullable false
     */
    @ApiModelProperty("语言编码")
    private String langCode;

    /**
     * 服务编码
     * <p>
     * length 30 0
     * nullable false
     */
    @ApiModelProperty("服务编码")
    private String serverKey;

    /**
     * 收件对象类型
     * <p>
     * length 20 0
     * nullable false
     */
    @ApiModelProperty("收件对象类型")
    private String receiverType;

    /**
     * 收件对象值
     * <p>
     * length 30 0
     * nullable false
     */
    @ApiModelProperty("收件对象值")
    private String receiverValue;

    /**
     * 消息优先级
     * <p>
     * length 10 0
     * nullable false
     */
    @ApiModelProperty("消息优先级")
    private Integer urgentLevel;

    @Override
    public String sendGroup() {
        return MessageInfoUtil.sendGroup(messageType, langCode);
    }

}
