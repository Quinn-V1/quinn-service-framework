package com.quinn.framework.component;

import com.quinn.framework.api.DynamicFilter;
import com.quinn.framework.model.QuinnFilterItem;
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
import java.util.*;

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

    /**
     * Spring 容器
     */
    private ApplicationContext applicationContext;

    /**
     * 配置条目
     */
    private List<QuinnFilterItem> filterItems;

    /**
     * 动态过滤器
     * 使用Bean 会导致 /** 跳过 anon 替代 afterPropertiesSet 中逻辑
     *         for (QuinnFilterItem filterItem : filterItems) {
     *             if (StringUtil.isNotEmpty(filterItem.getFilterName())) {
     *                 filterItem.setFilter(applicationContext.getBean(filterItem.getFilterName(), Filter.class));
     *             }
     *         }
     */
    private static final Map<String, DynamicFilter> DYNAMIC_FILTER_MAP = new HashMap<>();

    static {
        ServiceLoader<DynamicFilter> dynamicFilters = ServiceLoader.load(DynamicFilter.class);
        Iterator<DynamicFilter> dynamicFilterIterator = dynamicFilters.iterator();
        while (dynamicFilterIterator.hasNext()) {
            DynamicFilter next = dynamicFilterIterator.next();
            DYNAMIC_FILTER_MAP.put(next.name(), next);
        }
    }

    /**
     * Shiro过滤器增强配置
     * Filter filter = quinnFilterItem.getFilter();
     * FIXME
     * 配合 afterPropertiesSet 方法使用：但是存在问题 (会导致 /** 跳过 anon)
     *
     * @param shiroFilterFactoryBean Shiro过滤器
     */
    public void config(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filterMap = new HashMap<>(DYNAMIC_FILTER_MAP.size());
        Map<String, String> filterChainDefinition = new LinkedHashMap<>();
        for (QuinnFilterItem quinnFilterItem : filterItems) {
            String key = quinnFilterItem.getKey();
            Filter filter = DYNAMIC_FILTER_MAP.get(key);
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

    }
}
