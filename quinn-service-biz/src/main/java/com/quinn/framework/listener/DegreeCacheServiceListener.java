package com.quinn.framework.listener;

import com.quinn.framework.api.LoadOnStartDataService;
import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.framework.api.cache.CacheCommonService;
import com.quinn.framework.api.entityflag.CacheAble;
import com.quinn.framework.component.DegreeCacheService;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.model.BaseResult;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 多级缓存服务加载服务
 *
 * @author Qunhua.Liao
 * @since 2020-05-25
 */
@Component
@ConfigurationProperties(prefix = "com.quinn-service.cache.degree-cache")
public class DegreeCacheServiceListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(DegreeCacheServiceListener.class);

    @Resource
    private DegreeCacheService degreeCacheService;

    @Resource
    private CacheAllService cacheAllService;

    /**
     * 不同缓存级别（热度阈值）
     */
    private Map<String, Integer> itemMap;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (degreeCacheService == null || itemMap == null) {
            return;
        }

        // 等级缓存加载
        ApplicationContext applicationContext = event.getApplicationContext();
        for (Map.Entry<String, Integer> entry : itemMap.entrySet()) {
            try {
                CacheCommonService bean = applicationContext.getBean(entry.getKey(), CacheCommonService.class);
                degreeCacheService.addDegreeCache(entry.getValue(), bean);
            } catch (Exception e) {
                LOGGER.warn("cacheService【{0}】 in degreeCacheService not found", entry.getKey());
            }
        }

        // 启动即加载数据
        Map<String, LoadOnStartDataService> beans = applicationContext.getBeansOfType(LoadOnStartDataService.class);
        for (LoadOnStartDataService bean : beans.values()) {
            BaseResult<List<CacheAble>> hotDataRes = bean.selectHotData();
            if (!hotDataRes.isSuccess()) {
                continue;
            }

            for (CacheAble cacheAble : hotDataRes.getData()) {
                cacheAllService.set(cacheAble.cacheKey(), cacheAble);
            }
        }
    }

    public void setItemMap(Map<String, Integer> itemMap) {
        this.itemMap = itemMap;
    }
}
