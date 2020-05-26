package com.quinn.framework.rabbitmq.component;

import com.quinn.framework.api.ApplicationSerializer;
import com.quinn.framework.api.ConcurrentMqListener;
import com.quinn.util.base.model.BaseResult;

import java.lang.reflect.ParameterizedType;

/**
 * Rabbit MQ 监听器抽象类
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public abstract class AbstractRabbitMqListener<T> implements ConcurrentMqListener {

    {
        messageClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private Class<T> messageClass;

    private String targetQueueName;

    private int acknowledgeMode = ACK_MODE_AUTO;

    private ApplicationSerializer serializer;

    public AbstractRabbitMqListener(String queueName) {
        this(queueName, ACK_MODE_AUTO, null);
    }

    public AbstractRabbitMqListener(String queueName, int ackMode) {
        this(queueName, ackMode, null);
    }

    public AbstractRabbitMqListener(String queueName, int ackMode, ApplicationSerializer serializer) {
        this.targetQueueName = queueName;
        this.acknowledgeMode = ackMode;
        this.serializer = serializer;
    }

    @Override
    public int getListenerMode() {
        return acknowledgeMode;
    }

    @Override
    public String getTargetName() {
        return targetQueueName;
    }

    @Override
    public BaseResult handleMessage(Object object) {
        if (object instanceof byte[] && this.serializer != null) {
            doHandleMessage(serializer.deserialize((byte[]) object, messageClass));
        } else {
            doHandleMessage((T) object);
        }
        return BaseResult.SUCCESS;
    }

    @Override
    public int getConcurrent() {
        return 1;
    }

    /**
     * 实际处理逻辑
     *
     * @param object 消息数据
     */
    abstract void doHandleMessage(T object);
}
