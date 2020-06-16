package com.quinn.framework.model.deal;

import com.quinn.framework.util.BpmInstParamName;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.NumberUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.enums.CommonMessageEnum;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * BPM 流程处理参数
 *
 * @author Qunhua.Liao
 * @since 2020-05-01
 */
@Getter
@Setter
public abstract class AbstractBpmDealParam implements Serializable {

    /**
     * 每条执行流最后的处理任务
     */
    public static final String CACHE_KEY_OF_LAST_DEAL_PARAM = "BPM_LAST_DEAL_PARAM:";

    /**
     * 流程实例ID（草稿箱实例ID，如果不为空：草稿箱ID优先级最高）
     */
    @ApiModelProperty("草稿箱ID")
    private Long instanceId;

    /**
     * 模型ID（没有草稿箱ID：次高优先级）
     */
    @ApiModelProperty("模型ID")
    private Long taskId;

    /**
     * 节点编码
     */
    @ApiModelProperty("节点编码")
    private String nodeKey;

    /**
     * 处理用户
     */
    @ApiModelProperty("处理用户")
    private String dealUser;

    /**
     * 待办类型
     */
    @ApiModelProperty("待办类型")
    private String todoType;

    /**
     * 处理类型
     */
    @ApiModelProperty("处理类型")
    private String dealType;

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
     * 处理参数
     */
    @ApiModelProperty("实例参数")
    private Map<String, Object> instanceParams;

    /**
     * 紧急情况
     */
    @ApiModelProperty("紧急情况")
    private String urgentLevel;

    /**
     * 顶层组织
     */
    @ApiModelProperty("顶层组织")
    private String rootOrg;

    /**
     * 参数校验
     *
     * @return 校验结果
     */
    public BaseResult validate() {
        if (NumberUtil.isEmptyInFrame(taskId)) {
            return BaseResult.fail()
                    .buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.name(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], BpmInstParamName.TASK_ID)
                    .result();
        }
        return subValidate();
    }

    /**
     * 获取处理用户编码（通过会话添加一层保障）
     *
     * @return 用户编码
     */
    public String dealUserSafe() {
        return StringUtil.isEmpty(dealUser) ? SessionUtil.getUserKey() : dealUser;
    }

    /**
     * 获取处理用户顶层组织编码（通过会话添加一层保障）
     *
     * @return 用户编码
     */
    public String rootOrgSafe() {
        return StringUtil.isEmpty(rootOrg) ? SessionUtil.getOrgKey() : rootOrg;
    }

    /**
     * 子类校验
     *
     * @return 校验结果
     */
    protected abstract BaseResult subValidate();

    /**
     * 任务处理参数缓存
     *
     * @param taskKey BPM任务编码
     * @return  缓存键
     */
    public static String cacheKeyOf(String taskKey) {
        return CACHE_KEY_OF_LAST_DEAL_PARAM + taskKey;
    }
}
