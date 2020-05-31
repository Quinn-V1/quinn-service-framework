package com.quinn.framework.model;

import com.quinn.framework.api.ExecutionInfo;
import org.quartz.JobExecutionContext;

/**
 * Quartz 执行情况适配器
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public class QuartzExecutionInfoAdapter implements ExecutionInfo {

    /**
     * Quartz执行情况上下文
     */
    private JobExecutionContext jobExecutionContext;

    public QuartzExecutionInfoAdapter(JobExecutionContext jobExecutionContext) {
        this.jobExecutionContext = jobExecutionContext;
    }

}
