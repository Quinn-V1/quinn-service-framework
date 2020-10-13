package com.quinn.framework.model;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.BusinessJob;
import com.quinn.framework.api.JobHelpService;
import com.quinn.framework.api.JobInstance;
import com.quinn.framework.api.JobTemplate;
import com.quinn.framework.util.enums.JobStateEnum;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.handler.MultiMessageResolver;
import com.quinn.util.base.model.BaseResult;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 作业业务逻辑
 *
 * @author Qunhua.Liao
 * @since 2020-02-15
 */
public abstract class AbstractBusinessJob implements BusinessJob {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(AbstractBusinessJob.class);

    @Resource
    private JobHelpService jobHelpService;

    /**
     * 执行业务逻辑
     *
     * @param templateKey 任务模板
     * @return 执行是否成功
     */
    @Override
    public BaseResult<JobInstance> execute(String templateKey) {
        LOGGER.error("Job execute start of ${0}", templateKey);

        BaseResult<JobTemplate> res = jobHelpService.getTemplate(templateKey);
        if (!res.isSuccess()) {
            return BaseResult.fail(res.getMessage());
        }

        JobTemplate jobTemplate = res.getData();
        BaseResult<JobInstance> result = null;
        JobInstance jobInstance = JobInfoFactory.createJobInstance(jobTemplate);

        try {
            jobHelpService.saveInstance(jobInstance);

            // 补充参数
            BaseResult<Map<String, Object>> paramRes = jobHelpService.fillRunTimeParam(jobInstance);
            if (!paramRes.isSuccess()) {
                jobInstance.setExecResult(JobStateEnum.DONE_FAIL.code);
                result = BaseResult.fromPrev(paramRes);
                return result;
            }
            jobInstance.setExecParam(JSONObject.toJSONString(paramRes.getData()));

            // 调用业务逻辑
            result = doExecute(jobInstance, paramRes.getData());
            if (result.isSuccess()) {
                jobInstance.setExecResult(JobStateEnum.DONE_SUCCESS.code);
            } else {
                jobInstance.setExecResult(JobStateEnum.DONE_FAIL.code);
            }

        } catch (Exception e) {
            LOGGER.errorError("DistributeBusinessJob.execute failed", e);
            result = BaseResult.fromException(e);
            jobInstance.setExecResult(JobStateEnum.DONE_FAIL.code);
        } finally {
            // 保存任务实例
            jobInstance.setEndDateTime(LocalDateTime.now());
            String s = MultiMessageResolver.resolveResult(result);
            jobInstance.setExecMsg(StringUtil.cutFromLeftOfByte(s, 2000));

            jobHelpService.updateInstance(jobInstance);

            // 更新最新运行时间
            jobTemplate.lastExecute(jobInstance.getStartDateTime(), result.isSuccess());
            jobHelpService.updateTemplate(jobTemplate);
            LOGGER.error("Job execute end of ${0}", templateKey);
        }

        return result;
    }

    /**
     * 核心业务逻辑
     *
     * @param instance 消息实例
     * @param param    消息参数
     * @return 执行是否成功
     */
    public abstract BaseResult doExecute(final JobInstance instance, Map<String, Object> param);

}
