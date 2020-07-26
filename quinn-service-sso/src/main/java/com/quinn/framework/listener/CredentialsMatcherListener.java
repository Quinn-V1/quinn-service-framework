package com.quinn.framework.listener;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.AuthInfoSupplier;
import com.quinn.framework.api.CredentialsSubMatcher;
import com.quinn.framework.model.AuthInfoFactory;
import com.quinn.framework.util.MultiCredentialsMatcher;
import com.quinn.util.constant.enums.CommonMessageEnum;
import com.quinn.util.base.exception.MandatoryBeanMissException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

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
        Map<String, CredentialsSubMatcher> credentialsSubMatcherMap
                = contextRefreshedEvent.getApplicationContext().getBeansOfType(CredentialsSubMatcher.class);

        Map<Class, AuthInfoSupplier> authInfoSupplierMap = AuthInfoFactory.getAuthInfoSupplierMap();
        for (Map.Entry<Class, AuthInfoSupplier> entry : authInfoSupplierMap.entrySet()) {
            String beanName = entry.getValue().credentialsMatcherName();
            CredentialsSubMatcher credentialsSubMatcher = credentialsSubMatcherMap.get(beanName);

            if (credentialsSubMatcher == null) {
                throw new MandatoryBeanMissException()
                        .addParam(CommonMessageEnum.MANDATORY_BEAN_MISS.paramNames[0], beanName)
                        .addParam(CommonMessageEnum.MANDATORY_BEAN_MISS.paramNames[1], CredentialsSubMatcher.class)
                        .exception();
            }

            MultiCredentialsMatcher.addCredentialMatcher(entry.getKey(), credentialsSubMatcher);

            AuthInfo demo = entry.getValue().supply(null);
            if (demo != null) {
                MultiCredentialsMatcher.addCredentialMatcher(demo.getClass(), credentialsSubMatcher);
            }
        }
    }

}
