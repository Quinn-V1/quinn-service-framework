package com.quinn.framework.api.message;

/**
 * 消息发送记录
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageSendRecord {

    /**
     * 对应消息实例的编码
     *
     * @return 消息实例的编码
     */
    String sendGroup();

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
     * 消息服务服务编码
     *
     * @return 消息消息服务编码
     */
    String getServerKey();

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    String getMessageType();

    /**
     * 次级主键（MessageServer）
     */
    String subKey();

}
