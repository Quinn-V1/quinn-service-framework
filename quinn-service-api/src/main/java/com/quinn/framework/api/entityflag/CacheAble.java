package com.quinn.framework.api.entityflag;

/**
 * 可缓存标识
 *
 * @author Qunhua.Liao
 * @since 2020-03-28
 */
public interface CacheAble {

    /**
     * 获取系统主键
     *
     * @return 系统主键
     */
    Long getId();

    /**
     * 获取缓存键
     *
     * @return 缓存键
     */
    String cacheKey();

    /**
     * 获取对应实体类
     *
     * @return 实体类
     */
    default Class getEntityClass() {
        return this.getClass();
    }

}
