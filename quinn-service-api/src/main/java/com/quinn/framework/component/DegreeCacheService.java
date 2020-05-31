package com.quinn.framework.component;

import com.quinn.framework.api.cache.CacheCommonService;
import com.quinn.framework.model.HeatRateObject;
import com.quinn.util.base.api.MethodInvokerOneParam;

import java.util.ArrayList;

/**
 * 多级缓存服务
 *
 * @author Qunhua.Liao
 * @since 2020-05-24
 */
public class DegreeCacheService<T> {

    /**
     * 缓存服务列表：热度阈值越高，越排在前面
     */
    private ArrayList<DegreeCache> degreeCaches = new ArrayList<>();

    /**
     * 备用回调函数
     */
    private MethodInvokerOneParam<String, HeatRateObject<T>> backup;

    /**
     * 查找数据
     *
     * @param key 键
     * @return 值
     */
    public T get(String key) {
        for (DegreeCache cache : degreeCaches) {
            Object value = cache.cacheService.get(key);
            if (value != null) {
                return (T) value;
            }
        }

        HeatRateObject<T> value = backup.invoke(key);
        set(key, value.getData(), value.getHeatRate());
        return value.getData();
    }

    /**
     * 设置值
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, T value) {
        for (DegreeCache degreeCache : degreeCaches) {
            degreeCache.cacheService.set(key, value);
        }
    }

    /**
     * 设置值
     *
     * @param key      键
     * @param value    值
     * @param heatRate 热度
     */
    public void set(String key, T value, int heatRate) {
        for (int i = degreeCaches.size() - 1; i > -1; i--) {
            DegreeCache degreeCache = degreeCaches.get(i);
            if (degreeCache.heatRate > heatRate) {
                break;
            }
            degreeCache.cacheService.set(key, value);
        }
    }

    /**
     * 添加缓存服务
     * 热度越高越靠前（取值越容易）
     *
     * @param heatRate     热度阈值
     * @param cacheService 缓存服务
     */
    public void addDegreeCache(int heatRate, CacheCommonService cacheService) {
        for (int i = 0; i < degreeCaches.size(); i++) {
            DegreeCache degreeCache = degreeCaches.get(i);
            if (degreeCache.heatRate < heatRate) {
                degreeCaches.add(i, new DegreeCache(heatRate, cacheService));
                return;
            }
        }
        degreeCaches.add(new DegreeCache(heatRate, cacheService));
    }

    /**
     * 设置备用数据库查找方案
     *
     * @param backup 备用函数
     */
    public void ofBackup(MethodInvokerOneParam<String, HeatRateObject<T>> backup) {
        this.backup = backup;
    }

    /**
     * 缓存服务热度阈值包装
     *
     * @author Qunhua.Liao
     * @since 2020-05-24
     */
    private class DegreeCache {

        private DegreeCache(int heatRate, CacheCommonService cacheService) {
            this.heatRate = heatRate;
            this.cacheService = cacheService;
        }

        /**
         * 数据热度：查询次数 / 修改次数
         */
        private int heatRate;

        /**
         * 缓存服务
         */
        private CacheCommonService cacheService;

    }

}
