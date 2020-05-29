package com.quinn.framework.listener;

import com.quinn.framework.api.LoginPostProcessor;
import com.quinn.framework.api.LoginPrevProcessor;
import com.quinn.framework.service.SsoService;
import com.quinn.util.base.CollectionUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 登录增强器加载监听器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
@Component
public class LoginProcessorListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private SsoService ssoService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, LoginPostProcessor> postBean = event.getApplicationContext().getBeansOfType(LoginPostProcessor.class);
        Map<String, LoginPrevProcessor> prevBean = event.getApplicationContext().getBeansOfType(LoginPrevProcessor.class);

        if (!CollectionUtil.isEmpty(postBean)) {
            List<LoginPostProcessor> list = new ArrayList<>(postBean.values());
            Collections.sort(list, Comparator.comparingInt(LoginPostProcessor::priority));
            ssoService.setLoginPostProcessors(list);
        }

        if (!CollectionUtil.isEmpty(prevBean)) {
            List<LoginPrevProcessor> list = new ArrayList<>(prevBean.values());
            Collections.sort(list, Comparator.comparingInt(LoginPrevProcessor::priority));
            ssoService.setLoginPrevProcessors(list);
        }
    }

}
