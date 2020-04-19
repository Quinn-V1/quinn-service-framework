package com.quinn.framework.api.entityflag;

/**
 * 审计标识
 *
 * @author Qunhua.Liao
 * @since 2020-03-28
 */
public interface AuditAble {

    /**
     * 获取缓存键
     *
     * @return
     */
    String cacheKey();

}
