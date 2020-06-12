package com.quinn.framework.model;

import com.quinn.framework.util.enums.MessageThreadType;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 消息线程
 *
 * @author Qunhua.Liao
 * @since 2020-02-09
 */
@Setter
@Getter
public abstract class MessageThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageThread.class);

    /**
     * 消息收件对象处理接口
     */
    private CountDownLatch countDownLatchTotal;

    /**
     * 消息发送直接信息
     */
    protected DirectMessageInfo directMessageInfo;

    /**
     * 收件对象
     */
    protected MessageSendParam messageSendParam;

    /**
     * 有可能依赖外部线程《地址信息》
     */
    protected CountDownLatch latchForParam;

    /**
     * 有可能依赖外部线程《地址信息》
     */
    protected CountDownLatch latchForContent;

    /**
     * 有可能依赖外部线程《地址信息》
     */
    protected CountDownLatch latchForReceiver;

    /**
     * 处理业务
     */
    public abstract void handle();

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            handle();
        } catch (Exception e) {
            // FIXME
            LOGGER.error(this.getClass().getName() + "出错:", e);
            directMessageInfo.appendError(this.getClass().getName() + "出错:" + e.getMessage());
        } finally {
            countDownLatchTotal.countDown();
            // FIXME
            LOGGER.error("{} 耗时 {}", this.getClass().getName(), System.currentTimeMillis() - start);
        }
    }

    /**
     * 线程类型
     *
     * @return
     */
    public abstract MessageThreadType threadType();

}
