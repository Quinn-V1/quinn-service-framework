package com.quinn.framework.api.cache;

/**
 * 多级缓存具体条目
 *
 * @author Qunhua.Liao
 * @since 2020-05-25
 */
public interface DegreeCacheServiceItem {

    /**
     * 获取数据热度
     *
     * @return 数据热度阈值
     */
    int getHeatRate();

}
