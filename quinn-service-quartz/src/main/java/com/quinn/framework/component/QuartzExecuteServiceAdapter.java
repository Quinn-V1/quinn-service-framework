package com.quinn.framework.component;

import com.quinn.framework.api.*;
import com.quinn.framework.listener.QuartzJobListenerAdapter;
import com.quinn.framework.listener.QuartzTriggerListenerAdapter;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.MethodInvokerTwoParam;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.enums.MessageLevelEnum;
import lombok.SneakyThrows;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Quartz 任务执行器适配器
 *
 * @author Qunhua.Liao
 * @since 2020-5-30
 */
public class QuartzExecuteServiceAdapter implements JobExecuteService {

    @Resource
    private Map<String, BusinessJob> businessJobMap;

    @Resource
    private Scheduler scheduler;

    @Override
    public BaseResult addJob(JobTemplate jobTemplate) {
        String jobKey = jobTemplate.getScheduleKey();
        String jobImplement = jobTemplate.getImplementClass();
        String syncType = jobTemplate.getSyncType();

        BusinessJob businessJob = businessJobMap.get(jobImplement);
        if (businessJob == null) {
            // FIXME
            return BaseResult.fail("任务" + jobKey
                    + "初始化失败：实现Bean【" + jobImplement + "】不存在");
        }

        JobDetail jobDetail;
        if (!SpringBeanHolder.containsBean(jobKey)) {
            Map<String, Object> properties = new HashMap<>(8);
            properties.put("name", jobKey);

            // 此处本是 Scheduler.DEFAULT_GROUP
            properties.put("group", syncType);
            properties.put("targetMethod", "execute");
            properties.put("targetObject", businessJob);
            properties.put("arguments", new String[]{jobKey});

            jobDetail = SpringBeanHolder.registerBeanDefinition(jobKey,
                    MethodInvokingJobDetailFactoryBean.class, null, properties, true);
        } else {
            jobDetail = SpringBeanHolder.getBean(jobKey);
        }

        Trigger trigger = null;
        try {
            // Scheduler.DEFAULT_GROUP
            trigger = scheduler.getTrigger(TriggerKey.triggerKey(jobKey, syncType));
        } catch (SchedulerException e) {
        }

        if (trigger == null) {
            trigger = TriggerBuilder.newTrigger()
                    // Scheduler.DEFAULT_GROUP
                    .withIdentity(jobKey, syncType)
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobTemplate.getExecCycleExpress())).build();
        }


        String javaTimeZone = jobTemplate.getJavaTimeZone();
        if (StringUtil.isNotEmpty(javaTimeZone)) {
            ((CronTriggerImpl) trigger).setTimeZone(TimeZone.getTimeZone(javaTimeZone));
        }

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            return BaseResult.success(jobTemplate);
        } catch (SchedulerException e) {
            // FIXME
            return BaseResult.fail(e.getMessage());
        }
    }

    @Override
    public BaseResult executeJobDirect(JobTemplate jobTemplate) {
        BusinessJob businessJob = businessJobMap.get(jobTemplate.getImplementClass());
        if (businessJob == null) {
            // FIXME
            return BaseResult.fail("未找到实现类");
        }
        return businessJob.execute(jobTemplate.getScheduleKey());
    }

    @Override
    @SneakyThrows
    public BaseResult executeJob(JobTemplate jobTemplate) {
        String keyCode = jobTemplate.getScheduleKey();
        JobKey jobKey = JobKey.jobKey(keyCode);
        scheduler.triggerJob(jobKey);
        return BaseResult.SUCCESS;
    }

    @Override
    public BaseResult disableJob(JobTemplate jobTemplate) {
        return deleteJob(jobTemplate);
    }

    @Override
    public BaseResult enableJob(JobTemplate jobTemplate) {
        String keyCode = jobTemplate.getScheduleKey();
        JobKey jobKey = JobKey.jobKey(keyCode);
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return addJob(jobTemplate);
            } else {
                scheduler.resumeJob(jobKey);
                scheduler.resumeTrigger(TriggerKey.triggerKey(keyCode));
            }

            return BaseResult.success(jobTemplate);
        } catch (SchedulerException e) {
            // FIXME
            return BaseResult.fail(e.getMessage());
        }
    }

    @Override
    public BaseResult updateJob(JobTemplate jobTemplate) {
        String keyCode = jobTemplate.getScheduleKey();
        JobKey jobKey = JobKey.jobKey(keyCode);
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail != null) {
                scheduler.deleteJob(jobKey);
            }

            if (jobTemplate.isDisabled()) {
                return BaseResult.success(jobTemplate);
            }

            return addJob(jobTemplate);
        } catch (SchedulerException e) {
            // FIXME
            return BaseResult.fail(e.getMessage());
        }
    }

    @Override
    public BaseResult deleteJob(JobTemplate jobTemplate) {
        String keyCode = jobTemplate.getScheduleKey();
        JobKey jobKey = JobKey.jobKey(keyCode);
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return BaseResult.success(jobTemplate).ofLevel(MessageLevelEnum.WARN)
                        .buildMessage("", 0, 0).result();
            }

            boolean delete = scheduler.deleteJob(jobKey);
            return BaseResult.build(delete).ofData(jobTemplate);
        } catch (SchedulerException e) {
            // FIXME
            return BaseResult.fail(e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public void addJobListeners(List<SortJobListener> sortJobListeners) {
        for (SortJobListener sortJobListener : sortJobListeners) {
            scheduler.getListenerManager().addJobListener(new QuartzJobListenerAdapter(sortJobListener),
                    (Matcher<JobKey>) key -> {
                        MethodInvokerTwoParam<String, String, Boolean> effectStrategy = sortJobListener.effectStrategy();
                        if (effectStrategy == null) {
                            return true;
                        }
                        return effectStrategy.invoke(key.getGroup(), key.getGroup());
                    });
        }
    }

    @Override
    @SneakyThrows
    public void addTriggerListeners(List<SortTriggerListener> sortTriggerListeners) {
        for (SortTriggerListener sortTriggerListener : sortTriggerListeners) {
            scheduler.getListenerManager().addTriggerListener(new QuartzTriggerListenerAdapter(sortTriggerListener),
                    (Matcher<TriggerKey>) key -> {
                        MethodInvokerTwoParam<String, String, Boolean> effectStrategy = sortTriggerListener.effectStrategy();
                        if (effectStrategy == null) {
                            return true;
                        }
                        return effectStrategy.invoke(key.getName(), key.getGroup());
                    });
        }
    }

}
