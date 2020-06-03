package com.quinn.framework.api.message;

/**
 * 消息接受者
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageReceiver {

    /**
     * 设置消息接受者类型
     *
     * @param receiverType 消息接受类型
     */
    void setReceiverType(String receiverType);

    /**
     * 设置消息接收者值
     *
     * @param receiverValue 消息接收者值
     */
    void setReceiverValue(String receiverValue);

    /**
     * 用于追踪消息实例的编码
     *
     * @return 用于追踪消息实例的编码
     */
    String sendGroup();

}
