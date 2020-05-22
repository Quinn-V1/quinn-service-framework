package com.quinn.framework.listener;

import com.quinn.framework.api.CredentialsSubMatcher;
import com.quinn.framework.component.MultiCredentialsMatcher;
import com.quinn.util.base.model.ClassComparator;
import com.quinn.util.constant.enums.OrderByTypeEnum;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 证书比较器加载监听
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
@Component
public class CredentialsMatcherListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ClassComparator comparator = new ClassComparator(OrderByTypeEnum.ASC);
        Map<String, CredentialsSubMatcher> credentialsSubMatcherMap
                = contextRefreshedEvent.getApplicationContext().getBeansOfType(CredentialsSubMatcher.class);
        if (credentialsSubMatcherMap != null) {
            List<CredentialsSubMatcher> list = new ArrayList<>(credentialsSubMatcherMap.values());
            Collections.sort(list, comparator);

            for (CredentialsSubMatcher credentialsSubMatcher : credentialsSubMatcherMap.values()) {
                MultiCredentialsMatcher.addCredentialMatcher(credentialsSubMatcher);
            }
        }
    }

}
