package com.quinn.framework.component;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.MqService;
import com.quinn.framework.api.entityflag.CacheAble;
import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.framework.api.methodflag.WriteFlag;
import com.quinn.util.base.model.BaseResult;
import org.springframework.beans.factory.annotation.Value;

/**
 * 消息队列写拦截
 *
 * @author Qunhua.Liao
 * @since 2020-05-27
 */
public class MqWriteEntityServiceInterceptor implements EntityServiceInterceptor {

    public MqWriteEntityServiceInterceptor(MqService mqService) {
        this.mqService = mqService;
    }

    @Value("${com.quinn-service.mq.cache-able-exchange-type:FANOUT}")
    private String exchangeType;

    @Value("${com.quinn-service.mq.cache-able-exchange-name:eh-cache-refresh-listener}")
    private String exchangeName;

    @Value("${com.quinn-service.mq.cache-able-rout-key:}")
    private String routKey;

    @Value("${com.quinn-service.mq.cache-able-queue-name:eh-cache-refresh-listener}")
    private String[] queueNames;

    private MqService mqService;

    @Override
    public Class<? extends MethodFlag>[] interceptBean() {
        return new Class[]{CacheAble.class};
    }

    @Override
    public Class<? extends MethodFlag>[] interceptMethod() {
        return new Class[]{WriteFlag.class};
    }

    @Override
    public <V> void after(EntityServiceInterceptorChain chain, V t, BaseResult result) {
        mqService.send(t, exchangeType, exchangeName, routKey, queueNames);
    }

}
