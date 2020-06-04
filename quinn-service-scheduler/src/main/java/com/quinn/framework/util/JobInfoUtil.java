package com.quinn.framework.util;

import com.quinn.framework.api.JobInstance;
import com.quinn.framework.api.JobTemplate;
import com.quinn.framework.util.enums.JobStateEnum;
import com.quinn.util.constant.CommonParamName;
import com.quinn.util.constant.DateConstant;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.licence.model.ApplicationInfo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务工具类
 *
 * @author Qunhua.Liao
 * @since 2020-05-25
 */
public final class JobInfoUtil {

    private JobInfoUtil() {
    }

    /**
     * 使用任务模板信息填充任务实例（空）
     *
     * @param jobInstance 任务实例
     * @param jobTemplate 任务模板
     */
    public static void fillEmptyInstWithTemp(JobInstance jobInstance, JobTemplate jobTemplate) {
        jobTemplate.initDateTime(ApplicationInfo.getInstance().getStartDate());
        jobInstance.setApplicationKey(ApplicationInfo.getAppKey());
        jobInstance.setScheduleKey(jobTemplate.getScheduleKey());
        jobInstance.setStartDateTime(LocalDateTime.now());
        jobInstance.setExecResult(JobStateEnum.DOING.code);
        jobInstance.setExecMsg(StringConstant.NONE_OF_DATA);
        jobInstance.setExecParam(StringConstant.NONE_OF_DATA);
        jobInstance.setEndDateTime(DateConstant.MAX_DATE_TIME);
        jobInstance.setJobTemplate(jobTemplate);
    }

    /**
     * 初始化运行时参数
     *
     * @param jobInstance 运行实例
     * @return 运行时参数
     */
    public static Map<String, Object> initRunParams(JobInstance jobInstance) {
        Map<String, Object> res = new HashMap<>(8);
        JobTemplate jobTemplate = jobInstance.getJobTemplate();

        res.put(ScheduleParamName.PARAM_KEY_LAST_SUCCESS_DATETIME, jobTemplate.getLastSuccessDateTime());
        res.put(ScheduleParamName.PARAM_KEY_LAST_FAIL_DATETIME, jobTemplate.getLastFailDateTime());
        res.put(ScheduleParamName.PARAM_KEY_LAST_EXEC_DATETIME, jobTemplate.getLastExecDateTime());
        res.put(ScheduleParamName.PARAM_KEY_SCHEDULE_KEY, jobTemplate.getScheduleKey());
        res.put(CommonParamName.PARAM_KEY_NOW_TIME, jobInstance.getStartDateTime());
        return res;
    }
}
