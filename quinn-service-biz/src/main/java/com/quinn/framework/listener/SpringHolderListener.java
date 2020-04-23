package com.quinn.framework.listener;

import com.quinn.framework.api.CustomApplicationListener;
import com.quinn.framework.component.SpringBeanHolder;
import com.quinn.util.licence.model.ApplicationInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Bean持有容器监听
 *
 * @author Qunhua.Liao
 * @since 2020-04-23
 */
@Component
public class SpringHolderListener implements CustomApplicationListener {

    @Override
    public void applicationStarted(ApplicationContext applicationContext, ApplicationInfo applicationInfo) {
        SpringBeanHolder.setApplicationContext(applicationContext);
    }

}
