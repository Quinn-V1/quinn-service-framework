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
public enum BpmModelDataTypeEnum implements MessageEnumFlag {

    // 流程委托
    ModelAssignVO("BPM_MD_MODEL_ASSIGN", "流程委托"),

    // 流程表单
    ModelFormVO("BPM_MD_MODEL_FORM", "流程表单"),

    // 流程模板
    ModelInfoVO("BPM_MD_MODEL_INFO", "流程模板"),

    // 流程参数
    ModelParamVO("BPM_MD_MODEL_PARAM", "流程参数"),

    // 流程脚本
    ModelScriptVO("BPM_MD_MODEL_SCRIPT", "流程脚本"),

    // 流程节点候选人
    NodeCandidateVO("BPM_MD_NODE_CANDIDATE", "流程节点候选人"),

    // 流程节点
    NodeInfoVO("BPM_MD_NODE_INFO", "流程节点"),

    // 流程节点关系
    NodeRelateVO("BPM_MD_NODE_RELATE", "流程节点关系"),

    ;

    public final String code;

    public final String defaultDesc;

    BpmModelDataTypeEnum(String code, String defaultDesc) {
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
        EnumMessageResolver.addContent(Locale.SIMPLIFIED_CHINESE, BpmModelDataTypeEnum.values());
    }
}
