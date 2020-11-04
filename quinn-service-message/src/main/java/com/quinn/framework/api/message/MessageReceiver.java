package com.quinn.framework.api.message;

/**
 * 消息接受者
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageReceiver {

    /**
     * 收件人类型
     *
     * @return 收件人类型
     */
    String getReceiverType();

    /**
     * 设置消息接受者类型
     *
     * @param receiverType 消息接受类型
     */
    void setReceiverType(String receiverType);

    /**
     * 获取收件人值
     *
     * @return 收件人值
     */
    String getReceiverValue();

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

    /**
     * 获取消息类型
     *
     * @return 消息类型
     */
    String getMessageType();

    /**
     * 获取语言编码
     *
     * @return
     */
    String getLangCode();

    /**
     * 获取消息服务编码
     *
     * @return 消息服务编码
     */
    String getServerKey();

    /**
     * 获取消息紧急程度
     *
     * @return 消息紧急程度
     */
    Integer getUrgentLevel();

}
