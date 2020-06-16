package com.quinn.framework.util.enums;

import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.constant.MessageEnumFlag;

import java.util.Locale;

/**
 * BPM 模型 数据类型
 *
 * @author Qunhua.Liao
 * @since 2020-06-10
 */
public enum BpmInstanceDataTypeEnum implements MessageEnumFlag {

    // 流程实例参数
    BpmParamValueVO("BPM_IN_BPM_PARAM_VALUE", "流程实例参数"),

    // 流程脚本实例
    BpmScriptInstVO("BPM_IN_BPM_SCRIPT_INST", "流程脚本实例"),

    // 流程实例
    InstanceInfoVO("BPM_IN_INSTANCE_INFO", "流程实例"),

    // 任务候选人
    TaskCandidateVO("BPM_IN_TASK_CANDIDATE", "任务候选人"),

    // 流程任务处理
    TaskDealVO("BPM_IN_TASK_DEAL", "流程任务处理"),

    // 流程任务
    TaskInfoVO("BPM_IN_TASK_INFO", "流程任务"),

    ;

    public final String code;

    public final String defaultDesc;

    BpmInstanceDataTypeEnum(String code, String defaultDesc) {
        this.code = code;
        this.defaultDesc = defaultDesc;
    }

    @Override
    public String defaultDesc() {
        return this.defaultDesc;
    }

    @Override
    public String[] paramNames() {
        return null;
    }

    @Override
    public String key() {
        return CommonDataTypeEnum.wrapperKey(name());
    }

    static {
        EnumMessageResolver.addContent(Locale.SIMPLIFIED_CHINESE, BpmInstanceDataTypeEnum.values());
    }
}
