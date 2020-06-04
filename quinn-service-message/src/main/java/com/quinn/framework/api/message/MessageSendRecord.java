package com.quinn.framework.api.message;

/**
 * 消息发送记录
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageSendRecord {

    /**
     * 流程实例ID
     *
     * @return 流程实例ID
     */
    Long getMsgInstanceId();

    /**
     * 流程实例ID
     *
     * @param msgInstanceId 流程实例ID
     */
    void setMsgInstanceId(Long msgInstanceId);

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    String getMessageType();

    /**
     * 消息类型
     *
     * @param messageType 消息类型
     */
    void setMessageType(String messageType);

    /**
     * 消息服务服务编码
     *
     * @return 消息消息服务编码
     */
    String getServerKey();

    /**
     * 收件人编码（用户）
     *
     * @return 收件人编码（用户）
     */
    String getReceiverKey();

    /**
     * 对应消息实例的编码
     *
     * @return 消息实例的编码
     */
    String sendGroup();

    /**
     * 次级主键
     *
     * @return 次级主键
     * @see MessageServer
     */
    String subKey();

    /**
     * 获取消息实例
     *
     * @return 消息实例
     */
    MessageInstance getMessageInstance();

    /**
     * 设置消息实例
     *
     * @param instance 消息实例
     */
    void setMessageInstance(MessageInstance instance);

    /**
     * 获取收件人名称
     *
     * @return 收件人名称
     */
    String getReceiverName();

    /**
     * 获取收件人地址
     *
     * @return
     */
    String getReceiverAddress();

}
