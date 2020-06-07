package com.quinn.framework.api.message;

/**
 * 消息发送器生成工厂
 *
 * @author Qunhua.Liao
 * @since 2020-04-07
 */
public interface MessageInfoSupplier<I extends MessageInstance, S extends MessageSendRecord> {

    /**
     * 创建消息实例
     *
     * @return 消息实例
     */
    I createInstance();

    /**
     * 创建消息发送对象
     *
     * @return 消息发送对象
     */
    S createSendRecord();

}
