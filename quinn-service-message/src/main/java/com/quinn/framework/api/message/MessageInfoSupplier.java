package com.quinn.framework.api.message;

/**
 * 消息发送器生成工厂
 *
 * @author Qunhua.Liao
 * @since 2020-04-07
 */
public interface MessageInfoSupplier<I extends MessageInstance> {

    /**
     * 创建消息实例
     *
     * @return 消息实例
     */
    I createInstance();

}
