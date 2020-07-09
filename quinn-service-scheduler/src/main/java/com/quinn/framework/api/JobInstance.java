package com.quinn.framework.api;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 任务执行实例接口
 *
 * @author Qunhua.Liao
 * @since 2020-05-29
 */
public interface JobInstance<T extends JobTemplate> extends Serializable {

    /**
     * 设置执行任务的服务APP节点
     *
     * @param applicationKey 执行任务的服务APP节点
     */
    void setApplicationKey(String applicationKey);

    /**
     * 设置任务模板编码
     *
     * @param scheduleKey 任务模板编码
     */
    void setScheduleKey(String scheduleKey);

    /**
     * 设置执行参数
     *
     * @param execParam 执行参数
     */
    void setExecParam(String execParam);

    /**
     * 设置执行结果
     *
     * @param execResult 执行结果
     */
    void setExecResult(Integer execResult);

    /**
     * 设置执行消息
     *
     * @param execMsg 执行消息
     */
    void setExecMsg(String execMsg);

    /**
     * 设置开始时间
     *
     * @param now 开始时间
     */
    void setStartDateTime(LocalDateTime now);

    /**
     * 设置结束时间
     *
     * @param endDateTime 结束时间
     */
    void setEndDateTime(LocalDateTime endDateTime);

    /**
     * 获取开始时间
     *
     * @return 开始时间
     */
    LocalDateTime getStartDateTime();

    /**
     * 设置任务模板
     *
     * @param jobTemplate 任务模板
     */
    void setJobTemplate(T jobTemplate);

    /**
     * 获取任务模板
     *
     * @return 任务模板
     */
    JobTemplate getJobTemplate();

    /**
     * 获取运行时参数
     *
     * @return 运行时参数
     */
    default Map<String, Object> getRuntimeParams() {
        return null;
    }

}
