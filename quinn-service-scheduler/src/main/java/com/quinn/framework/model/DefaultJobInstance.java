package com.quinn.framework.model;

import com.quinn.framework.api.JobInstance;
import com.quinn.framework.api.JobTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 任务执行实例
 *
 * @author Qunhua.Liao
 * @since 2020-02-08
 */
@Setter
@Getter
@ApiModel("任务执行实例")
public class DefaultJobInstance implements JobInstance {

    /**
     * 应用节点
     */
    @ApiModelProperty("应用节点")
    private String applicationKey;

    /**
     * 任务模板编码
     */
    @ApiModelProperty("任务ID")
    private String scheduleKey;

    /**
     * 执行开始时间
     */
    @ApiModelProperty("执行开始时间")
    private LocalDateTime startDateTime;

    /**
     * 执行结束时间
     */
    @ApiModelProperty("执行结束时间")
    private LocalDateTime endDateTime;

    /**
     * 执行结果
     */
    @ApiModelProperty("执行状态")
    private Integer execResult;

    /**
     * 执行消息
     */
    @ApiModelProperty("执行消息")
    private String execMsg;

    /**
     * 执行参数
     */
    @ApiModelProperty("执行参数")
    private String execParam;

    /**
     * 任务模板
     */
    @ApiModelProperty("任务模板")
    private JobTemplate jobTemplate;

}
