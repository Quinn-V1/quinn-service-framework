package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

/**
 * 消息队列监听器（被动方）
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public interface MqListener {

    int ACK_MODE_AUTO = 0;

    int ACK_MODE_MANUAL = 1;

    int ACK_MODE_NONE = 2;

    /**
     * 处理消息
     *
     * @param message 消息体
     * @return 处理结果
     */
    BaseResult handleMessage(Object message);

    /**
     * 获取消息监听对象
     *
     * @return 消息监听对象
     */
    String getTargetName();

    /**
     * 获取监听模式（是否自动ACK）
     *
     * @return 监听模式
     */
    int getListenerMode();

}
