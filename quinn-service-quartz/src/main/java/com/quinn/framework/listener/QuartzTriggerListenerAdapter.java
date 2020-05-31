package com.quinn.framework.listener;

import com.quinn.framework.api.SortTriggerListener;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

/**
 * 任务监听适配器
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public class QuartzTriggerListenerAdapter implements TriggerListener {

    public QuartzTriggerListenerAdapter(SortTriggerListener sortTriggerListener) {
        this.sortTriggerListener = sortTriggerListener;
    }

    private SortTriggerListener sortTriggerListener;

    @Override
    public String getName() {
        return "quartzTriggerListenerAdapter";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {

    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        trigger.getJobKey().getName();
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
                                Trigger.CompletedExecutionInstruction triggerInstructionCode) {

    }

}
