package com.quinn.framework.api;

/**
 * 分布式锁
 *
 * @author Qunhua.Liao
 * @since 2020-02-15
 */
public interface DistributeLock {

    /**
     * 获取锁
     *
     * @param key 业务主键
     * @return 是否获取成功
     */
    boolean lock(String key);

    /**
     * 获取锁
     *
     * @param key                业务主键
     * @param selfReleaseExpired 业务主键
     * @return 是否获取成功
     */
    boolean tryLock(String key, long selfReleaseExpired);

    /**
     * 释放锁
     *
     * @param key 业务主键
     * @return 是否释放成功
     */
    boolean unLock(String key);

    /**
     * 查询谁拥有锁
     *
     * @param key 业务主键
     * @return 是否获取成功
     */
    String whoHasLock(String key);

    /**
     * 远程锁定
     *
     * @param key 业务主键
     * @return 是否获取锁成功
     */
    boolean lockRemote(String key);

    /**
     * 本地锁定
     *
     * @param key 业务主键
     * @return 是否获取锁成功
     */
    boolean lockLocal(String key);

}
