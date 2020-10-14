package com.quinn.framework.component;

import com.quinn.framework.api.*;
import com.quinn.framework.model.JobInfoFactory;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.model.BaseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.util.*;

/**
 * 定时任务监听器
 *
 * @author Qunhua.Liao
 * @since 2020-02-15
 */
public class JobLoadOnStartedListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(JobLoadOnStartedListener.class);

    @Value("${com.quinn-service.schedule.strict-mode:false}")
    private boolean strictMode;

    @Resource
    private JobHelpService jobHelpService;

    @Resource
    private JobExecuteService jobExecuteService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Map<String, BusinessJob> beansOfType = event.getApplicationContext().getBeansOfType(BusinessJob.class);
        JobInfoFactory.addAll(beansOfType);

        BaseResult<List<JobTemplate>> result = jobHelpService.findAvailableTemplate();
        if (!result.isSuccess()) {
            LOGGER.warn("No JobTemplate configured in the application context");
            return;
        }

        // 注册任务
        List<JobTemplate> jobs = result.getData();
        if (null != jobs) {
            for (JobTemplate job : jobs) {
                try {
                    BaseResult res = jobExecuteService.addJob(job);
                    if (!res.isSuccess()) {
                        if (strictMode && !res.wantContinue()) {
                            LOGGER.error("Schedule job {} add failed for reason {}", job.getScheduleKey(),
                                    res.getMessage());
                            System.exit(-1);
                        } else {
                            LOGGER.warn("Schedule job {} add failed for reason {}", job.getScheduleKey(),
                                    res.getMessage());
                        }
                    }
                } catch (Exception e) {
                    if (strictMode) {
                        LOGGER.error("Schedule job {} add failed for reason {}", job.getScheduleKey(),
                                e.getMessage());
                        System.exit(-1);
                    } else {
                        LOGGER.warn("Schedule job {} add failed for reason {}", job.getScheduleKey(),
                                e.getMessage());
                    }
                }
            }
        }

        // 添加工作监听
        Map<String, SortJobListener> sortJobListenerMap = event.getApplicationContext()
                .getBeansOfType(SortJobListener.class);
        List<SortJobListener> sortJobListeners = new ArrayList<>(sortJobListenerMap.values());
        Collections.sort(sortJobListeners, Comparator.comparingInt(SortJobListener::getOrder));

        jobExecuteService.addJobListeners(sortJobListeners);

        // 添加触发监听器
        Map<String, SortTriggerListener> sortTriggerListenerMap = event.getApplicationContext()
                .getBeansOfType(SortTriggerListener.class);
        List<SortTriggerListener> sortTriggerListeners = new ArrayList<>(sortTriggerListenerMap.values());
        Collections.sort(sortTriggerListeners, Comparator.comparingInt(SortTriggerListener::getOrder));

        jobExecuteService.addTriggerListeners(sortTriggerListeners);
    }

}
