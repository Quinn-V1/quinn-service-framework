package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

/**
 * 业务任务接口类
 * 一个任务类可能被赋予不同的参数安排多次运行
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public interface BusinessJob {

    /**
     * 执行任务
     *
     * @param jobTemplateKey 任务编码
     * @return 执行记录
     */
    BaseResult<JobInstance> execute(String jobTemplateKey);

}
