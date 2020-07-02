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

    // 待审批
    AUDIT(BpmDealTypeEnum.AGREE.name()),

    // 等待反馈
    AUDIT_WFB(BpmDealTypeEnum.AGREE.name()),

    // 待阅
    READ(BpmDealTypeEnum.READ.name()),

    // 待反馈
    FEEDBACK(BpmDealTypeEnum.FEEDBACK.name()),

    ;

    /**
     * 编码
     */
    public final String defaultDealType;

    BpmTodoTypeEnum(String defaultDealType) {
        this.defaultDealType = defaultDealType;
    }

    /**
     * 待办是否为审批类型
     *
     * @param todoType 待办类型
     * @return 是否为审批类型
     */
    public static boolean isAudit(String todoType) {
        return todoType.startsWith(AUDIT.name());
    }

    /**
     * 默认处理方式
     *
     * @param todoType 待办类型
     * @param manual   是否手动干预
     */
    public static String defaultDealTypeOf(String todoType, boolean manual) {
        BpmTodoTypeEnum bpmTodoTypeEnum = BpmTodoTypeEnum.valueOf(todoType);
        if (bpmTodoTypeEnum.ordinal() >= AUDIT.ordinal()) {
            return bpmTodoTypeEnum.defaultDealType;
        } else if (manual) {
            return null;
        }
        return bpmTodoTypeEnum.defaultDealType;
    }
}
