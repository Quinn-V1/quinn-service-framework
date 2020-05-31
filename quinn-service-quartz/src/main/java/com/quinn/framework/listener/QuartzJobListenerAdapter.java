package com.quinn.framework.listener;

import com.quinn.framework.api.SortJobListener;
import com.quinn.framework.model.QuartzExecutionInfoAdapter;
import com.quinn.util.base.exception.BaseWrapperException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * 任务监听适配器
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public class QuartzJobListenerAdapter implements JobListener {

    private SortJobListener sortJobListener;

    public QuartzJobListenerAdapter(SortJobListener sortJobListener) {
        this.sortJobListener = sortJobListener;
    }

    @Override
    public String getName() {
        return sortJobListener.getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        sortJobListener.jobToBeExecuted(new QuartzExecutionInfoAdapter(jobExecutionContext));
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        sortJobListener.jobExecutionVetoed(new QuartzExecutionInfoAdapter(jobExecutionContext));
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        sortJobListener.jobWasExecuted(new QuartzExecutionInfoAdapter(jobExecutionContext),
                new BaseWrapperException(e)
        );
    }

}
