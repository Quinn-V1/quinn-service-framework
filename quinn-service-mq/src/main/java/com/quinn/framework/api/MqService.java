package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

/**
 * 消息队列服务（主动方）
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public interface MqService {

    /**
     * 信息发送(exchangeType:direct)
     *
     * @param queueName queueName名称
     * @param data      发送数据对象
     * @return 发送结果
     */
    BaseResult send(String queueName, Object data);

    /**
     * 信息发送
     *
     * @param target 信息类型对象
     * @param data   数据对象
     * @return 发送结果
     */
    BaseResult send(MqTarget target, Object data);

    /**
     * 注册消费者
     *
     * @param listener 消费者实例
     * @return 注册结果
     */
    BaseResult listen(MqListener listener);

    /**
     * 注册消费者
     *
     * @param l                   消费者实例
     * @param concurrentConsumers 开几个线程来监听
     * @return 注册结果
     */
    BaseResult listen(MqListener l, Integer concurrentConsumers);

    /**
     * 主动接收消息
     *
     * @param listener    消息监听器
     * @param threadCount 接收消息的线程数
     * @return 发送结果
     */
    BaseResult receive(MqListener listener, int threadCount);

    /**
     * 移除消费者
     *
     * @param listener 消费者实例
     * @return 移除是否成功
     */
    BaseResult remove(MqListener listener);
}
