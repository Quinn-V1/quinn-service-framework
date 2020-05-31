package com.quinn.framework.model;

import com.quinn.framework.api.JobTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 任务模板
 *
 * @author Qunhua.Liao
 * @since 2020-02-15
 */
@Setter
@Getter
@ApiModel("任务模板")
public class DefaultJobTemplate implements JobTemplate {

    /**
     * 定时任务编码
     */
    @ApiModelProperty("定时任务编码")
    private String scheduleKey;

    /**
     * 业务实现类
     */
    @ApiModelProperty("业务实现类")
    private String implementClass;

    /**
     * Java时区
     */
    @ApiModelProperty("Java时区")
    private String javaTimeZone;

    /**
     * 同步类型
     */
    @ApiModelProperty("同步类型")
    private Integer syncType;

    /**
     * 周期表达式
     */
    @ApiModelProperty("周期表达式")
    private String execCycleExpress;

    /**
     * 周期表达式描述
     */
    @ApiModelProperty("周期表达式描述")
    private String cycleDesc;

    /**
     * 随后执行成功时间
     */
    @ApiModelProperty("随后执行成功时间")
    private LocalDateTime lastSuccessDateTime;

    /**
     * 随后执行失败时间
     */
    @ApiModelProperty("随后执行失败时间")
    private LocalDateTime lastFailDateTime;

    /**
     * 随后执行时间
     */
    @ApiModelProperty("随后执行时间")
    private LocalDateTime lastExecDateTime;

    /**
     * 任务参数
     */
    @ApiModelProperty("任务参数")
    private String jobParameter;

    /**
     * 合并新的属性
     *
     * @param template 新的属性
     */
    public void combineNewProp(DefaultJobTemplate template) {
        if (!StringUtils.isEmpty(template.getImplementClass())) {
            setImplementClass(template.getImplementClass());
        }

        if (!StringUtils.isEmpty(template.getExecCycleExpress())) {
            setExecCycleExpress(template.getExecCycleExpress());
        }

        if (!StringUtils.isEmpty(template.getJobParameter())) {
            setJobParameter(template.getJobParameter());
        }
    }

//    public boolean diffImportant(JobTemplate jobTemplate) {
//        return BaseUtil.diff(jobParameter, jobTemplate.jobParameter) ||
//                BaseUtil.diff(execCycle, jobTemplate.execCycle) ||
//                BaseUtil.diff(jobImplement, jobTemplate.jobImplement)
//                ;
//    }
//
//    public boolean isDisabled() {
//        return (getTombstone() != null && getTombstone().intValue() == DataStateEnum.DISABLE.code) ||
//                (getEnable() != null && getEnable().intValue() == DataStateEnum.DISABLE.code);
//    }
//
//    /**
//     * 获取任务编码
//     *
//     * @return 任务编码
//     */
//    public String getJobKey() {
//        return getKeyCode();
//    }
//
//    /**
//     * 同步类型枚举类
//     *
//     * @return 枚举类
//     */
//    public SyncTypeEnum syncTypeEnum() {
//        return SyncTypeEnum.codeToEnum(syncType);
//    }
//
//    /**
//     * 转为任务实例
//     *
//     * @return 任务实例
//     */
//    public JobInstance toInstance() {
//        JobInstance instance = new JobInstance();
//        instance.setJobTemplate(this);
//        instance.setKeyCode(BaseUtil.uuid().substring(10));
//        instance.setStartTime(new Date());
//        instance.setJobId(this.getId());
//        instance.setResult(JobStateEnum.DOING);
//        return instance;
//    }
}
