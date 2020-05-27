package com.quinn.framework.api;

import com.quinn.util.constant.NumberConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.ParameterizedType;

/**
 * Rabbit MQ 监听器抽象类
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
@Getter
@Setter
public abstract class AbstractMqListener<T> implements ConcurrentMqListener, InitializingBean {

    {
        messageClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 消息实际类型
     */
    private Class<T> messageClass;

    /**
     * 监听目标对象
     */
    private String targetName;

    /**
     * 消息消除模式
     */
    private int listenerMode = ACK_MODE_AUTO;

    /**
     * 消息序器
     */
    private ApplicationSerializer serializer;

    @Override
    public void handleMessage(Object object) {
        if (object instanceof byte[] && this.serializer != null) {
            doHandleMessage(serializer.deserialize((byte[]) object, messageClass));
        } else {
            doHandleMessage((T) object);
        }
    }

    @Override
    public int getConcurrent() {
        return NumberConstant.INT_ONE;
    }

    /**
     * 实际处理逻辑
     *
     * @param object 消息数据
     */
    public abstract void doHandleMessage(T object);
}
