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
    String instanceKey();

}
