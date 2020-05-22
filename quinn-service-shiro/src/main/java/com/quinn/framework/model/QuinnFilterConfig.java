package com.quinn.framework.model;

import com.quinn.util.base.StringUtil;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro过滤器增强配置
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
@Component
@ConfigurationProperties(prefix = "com.quinn-service.filter.config")
public class QuinnFilterConfig implements ApplicationContextAware, InitializingBean {

    @Resource
    private SecurityManager securityManager;

    private ApplicationContext applicationContext;

    private List<QuinnFilterItem> filterItems;

    /**
     * Shiro过滤器增强配置
     *
     * @param shiroFilterFactoryBean Shiro过滤器
     */
    public void config(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filterMap = new HashMap<>(filterItems.size());
        Map<String, String> filterChainDefinition = new LinkedHashMap<>();
        for (QuinnFilterItem quinnFilterItem : filterItems) {
            String key = quinnFilterItem.getKey();
            Filter filter = quinnFilterItem.getFilter();
            if (filter != null) {
                filterMap.put(key, filter);
            }

            List<String> urlPatterns = quinnFilterItem.getUrlPattens();
            if (urlPatterns != null) {
                for (String urlPatter : urlPatterns) {
                    filterChainDefinition.put(urlPatter, key);
                }
            }
        }

        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinition);
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public List<QuinnFilterItem> getFilterItems() {
        return filterItems;
    }

    public void setFilterItems(List<QuinnFilterItem> filterItems) {
        this.filterItems = filterItems;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        for (QuinnFilterItem filterItem : filterItems) {
            if (StringUtil.isNotEmpty(filterItem.getFilterName())) {
                filterItem.setFilter(applicationContext.getBean(filterItem.getFilterName(), Filter.class));
            }
        }
    }
}
