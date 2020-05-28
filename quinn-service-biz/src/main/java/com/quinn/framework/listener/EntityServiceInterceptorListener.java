package com.quinn.framework.listener;

import com.quinn.framework.api.ErrorHandler;
import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.util.MultiErrorHandler;
import com.quinn.framework.service.BaseEntityService;
import com.quinn.util.base.ClassUtil;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.constant.enums.OrderByTypeEnum;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 实体业务拦截器注册监听
 *
 * @author Qunhua.Liao
 * @since 2020-04-08
 */
@Component
public class EntityServiceInterceptorListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, EntityServiceInterceptor> interceptorMap = event.getApplicationContext()
                .getBeansOfType(EntityServiceInterceptor.class);

        Map<String, BaseEntityService> entityServiceMap = event.getApplicationContext()
                .getBeansOfType(BaseEntityService.class);

        if (interceptorMap != null && entityServiceMap != null) {
            Collection<EntityServiceInterceptor> interceptors = interceptorMap.values();
            for (BaseEntityService object : entityServiceMap.values()) {
                for (EntityServiceInterceptor entityServiceInterceptor : interceptors) {
                    if (ClassUtil.classIn(object.getVOClass(), entityServiceInterceptor.interceptBean())) {
                        object.addEntityServiceInterceptor(entityServiceInterceptor);
                    }
                }
            }
        }

        Map<String, ErrorHandler> handlerMap = event.getApplicationContext().getBeansOfType(ErrorHandler.class);
        if (!CollectionUtil.isEmpty(handlerMap)) {
            List<ErrorHandler> sort = ClassUtil.sort(handlerMap.values(), OrderByTypeEnum.ASC);
            MultiErrorHandler.addHandlers(sort);
        }
    }

}
