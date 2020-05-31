package com.quinn.framework.api;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务模板接口
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public interface JobTemplate extends Serializable {

    /**
     * 获取任务模板编码
     *
     * @return 任务模板编码
     */
    String getScheduleKey();

    /**
     * 获取任务实现类
     *
     * @return 任务实现类
     */
    String getImplementClass();

    /**
     * 获取执行周期 cron 表达式
     *
     * @return 执行周期
     */
    String getExecCycleExpress();

    /**
     * 获取Java时区
     *
     * @return Java时区
     */
    String getJavaTimeZone();

    /**
     * 最后成功时间
     *
     * @param dateTime 实行时间
     */
    void setLastSuccessDateTime(LocalDateTime dateTime);

    /**
     * 最后失败时间
     *
     * @param dateTime 实行时间
     */
    void setLastFailDateTime(LocalDateTime dateTime);

    /**
     * 最后执行时间
     *
     * @param dateTime 实行时间
     */
    void setLastExecDateTime(LocalDateTime dateTime);

    /**
     * 最后成功时间
     *
     * @return 最后成功时间
     */
    LocalDateTime getLastSuccessDateTime();

    /**
     * 最后失败时间
     *
     * @return 最后失败时间
     */
    LocalDateTime getLastFailDateTime();

    /**
     * 最后执行时间
     *
     * @return 最后执行时间
     */
    LocalDateTime getLastExecDateTime();

    /**
     * 获取参数策略
     *
     * @return 参数策略
     */
    String getParamStrategy();

    /**
     * 执行次数加 1
     */
    default void execIncrease() {}

    /**
     * 设置最后执行情况
     *
     * @param dateTime 执行时间
     * @param success  执行结果
     */
    default void lastExecute(LocalDateTime dateTime, boolean success) {
        setLastExecDateTime(dateTime);
        execIncrease();
        if (success) {
            setLastSuccessDateTime(dateTime);
        } else {
            setLastFailDateTime(dateTime);
        }
    }

    /**
     * 设置最后执行情况
     *
     * @param dateTime 初始化时间
     */
    default void initDateTime(LocalDateTime dateTime) {
        if (getLastExecDateTime() == null) {
            setLastExecDateTime(dateTime);
        }

        if (getLastSuccessDateTime() == null) {
            setLastSuccessDateTime(dateTime);
        }

        if (getLastFailDateTime() == null) {
            setLastFailDateTime(dateTime);
        }
    }

}
