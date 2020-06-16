package com.quinn.framework.model.deal;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.NumberUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.enums.CommonMessageEnum;
import com.quinn.util.base.exception.ParameterShouldNotEmpty;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.UrgentLevelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程启动参数
 *
 * @author Qunhua.Liao
 * @since 2020-05-01
 */
@Getter
@Setter
public class BpmStartParam {

    /**
     * 流程实例ID（草稿箱实例ID，如果不为空：草稿箱ID优先级最高）
     */
    @ApiModelProperty("草稿箱ID")
    private Long boxId;

    /**
     * 模型ID（没有草稿箱ID：次高优先级）
     */
    @ApiModelProperty("模型ID")
    private Long modelId;

    /**
     * 模型编码（如果模型ID为空，则按编码发起：如果编码也为空报错）
     */
    @ApiModelProperty("模型编码")
    private String modelKey;

    /**
     * 模型版本（版本为空：则按最新版本进行发起：如果不为空，则按版本号发起）
     */
    @ApiModelProperty("模型版本")
    private Integer modelVersion;

    /**
     * 发起用户
     */
    @ApiModelProperty("发起用户")
    private String startUser;

    /**
     * 紧急情况
     */
    @ApiModelProperty("紧急情况")
    private String urgentLevel;

    /**
     * 业务系统
     */
    @ApiModelProperty("业务系统")
    private String systemKey;

    /**
     * 业务主键
     */
    @ApiModelProperty("业务主键")
    private String businessKey;

    /**
     * 流程标题
     */
    @ApiModelProperty("流程标题")
    private String subject;

    /**
     * 处理意见
     */
    @ApiModelProperty("处理意见")
    private String suggestion;

    /**
     * 附件组
     */
    @ApiModelProperty("附件组")
    private String fileGroup;

    /**
     * 实例参数
     */
    @ApiModelProperty("实例参数")
    private JSONObject instanceParams;

    /**
     * 顶层组织
     */
    @ApiModelProperty("顶层组织")
    private String rootOrg;

    /**
     * 全业务主键
     *
     * @return 全业务主键
     */
    public String getFullBusinessKey() {
        if (StringUtil.isEmptyInFrame(systemKey)) {
            if (StringUtil.isEmptyInFrame(businessKey)) {
                return null;
            } else {
                return businessKey;
            }
        } else {
            if (StringUtil.isEmptyInFrame(businessKey)) {
                return systemKey;
            } else {
                return systemKey + StringConstant.CHAR_AT_SING + businessKey;
            }
        }
    }

    /**
     * 是否已经正式校验
     */
    @JsonIgnore
    private boolean validated;

    /**
     * 正式校验
     */
    public void validate() {
        validateForBox();

        validated = true;
    }

    /**
     * 草稿箱校验
     */
    public void validateForBox() {
        if (validated) {
            return;
        }

        // 流程模型标识不能为空
        if (NumberUtil.isEmptyInFrame(modelId) && StringUtil.isEmpty(modelKey)) {
            throw new ParameterShouldNotEmpty()
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "modelId | modelKey")
                    .exception();
        }

        if (StringUtil.isEmpty(systemKey)) {
            throw new ParameterShouldNotEmpty()
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "systemKey")
                    .exception();
        }

        if (StringUtil.isEmpty(businessKey)) {
            throw new ParameterShouldNotEmpty()
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "businessKey")
                    .exception();
        }
    }

    /**
     * 空安全获取用户
     *
     * @return 用户编码
     */
    public String startUserOfSafe() {
        return StringUtil.isEmptyInFrame(startUser) ? SessionUtil.getUserKey() : startUser;
    }
}
