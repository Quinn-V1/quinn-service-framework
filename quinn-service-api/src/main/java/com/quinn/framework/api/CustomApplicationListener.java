package com.quinn.framework.api;

import com.quinn.util.licence.model.ApplicationInfo;
import org.springframework.context.ApplicationContext;

/**
 * 自定义应用监听器
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public interface CustomApplicationListener {

    /**
     * 应用启动监听
     *
     * @param applicationContext    应用容器
     * @param applicationInfo       应用服务
     */
    void applicationStarted(ApplicationContext applicationContext, ApplicationInfo applicationInfo);

}
