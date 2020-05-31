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

}
