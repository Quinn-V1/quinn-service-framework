package com.quinn.framework.model;

import com.quinn.framework.api.JobTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
    private String syncType;

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
    private String paramStrategy;

}
