package com.quinn.framework.util.enums;

/**
 * BPM 待办类型（某些场景可多选，支持位存储）
 *
 * @author Qunhua.Liao
 * @since 2020-05-12
 */
public enum BpmTodoTypeEnum {

    // 跳过
    SKIP(BpmDealTypeEnum.SKIP.name()),

    // 系统自动
    AUTO(BpmDealTypeEnum.AUTO.name()),

    // 系统专办
    AUDIT(BpmDealTypeEnum.AGREE.name()),

    // 人工同意
    READ(BpmDealTypeEnum.READ.name()),

    // 沟通
    FEEDBACK(BpmDealTypeEnum.FEEDBACK.name()),

    ;

    /**
     * 编码
     */
    public final String defaultDealType;

    BpmTodoTypeEnum(String defaultDealType) {
        this.defaultDealType = defaultDealType;
    }

}
