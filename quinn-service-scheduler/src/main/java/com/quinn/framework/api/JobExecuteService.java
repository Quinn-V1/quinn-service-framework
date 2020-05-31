package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

import java.util.List;

/**
 * 任务执行业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public interface JobExecuteService {

    /**
     * 添加任务
     *
     * @param jobTemplate 任务模板
     * @return 添加结果
     */
    BaseResult addJob(JobTemplate jobTemplate);

    /**
     * 添加监听器
     *
     * @param sortJobListeners 监听器
     */
    void addJobListeners(List<SortJobListener> sortJobListeners);

    /**
     * 添加监听器
     *
     * @param sortTriggerListeners 监听器
     */
    void addTriggerListeners(List<SortTriggerListener> sortTriggerListeners);

    /**
     * 执行任务
     *
     * @param jobTemplate 任务模板
     * @return 任务实例
     */
    BaseResult executeJob(JobTemplate jobTemplate);

    /**
     * 直接执行任务，不交给调度系统
     *
     * @param jobTemplate 任务模板
     * @return 任务实例
     */
    BaseResult executeJobDirect(JobTemplate jobTemplate);

    /**
     * 禁用任务
     *
     * @param jobTemplate 任务模板
     * @return 禁用结果
     */
    BaseResult disableJob(JobTemplate jobTemplate);

    /**
     * 启用任务
     *
     * @param jobTemplate 任务模板
     * @return 启用结果
     */
    BaseResult enableJob(JobTemplate jobTemplate);

    /**
     * 更新任务
     *
     * @param jobTemplate 任务模板
     * @return 更新结果
     */
    BaseResult updateJob(JobTemplate jobTemplate);

    /**
     * 删除任务
     *
     * @param jobTemplate 任务模板
     * @return 删除结果
     */
    BaseResult deleteJob(JobTemplate jobTemplate);
}
