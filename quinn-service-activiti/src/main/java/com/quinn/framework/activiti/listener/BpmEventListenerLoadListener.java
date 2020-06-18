package com.quinn.framework.activiti.listener;

import com.quinn.framework.activiti.model.ActToBpmInfoFactory;
import com.quinn.framework.api.BpmInstSupplier;
import com.quinn.framework.api.CustomBpmEventListener;
import com.quinn.util.base.CollectionUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Bpm事件监听器加载-监听器
 *
 * @author Qunhua.Liao
 * @since 2020-05-06
 */
@Component
public class BpmEventListenerLoadListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private BpmInstSupplier bpmInstSupplier;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, CustomBpmEventListener> beansOfType = event.getApplicationContext()
                .getBeansOfType(CustomBpmEventListener.class);

        if (!CollectionUtil.isEmpty(beansOfType)) {
            for (CustomBpmEventListener listener : beansOfType.values()) {
                GlobalBpmListener.addCustomBpmEventListener(listener.eventType(), listener);
            }
        }

        ActToBpmInfoFactory.setBpmInstSupplier(bpmInstSupplier);
    }
}
