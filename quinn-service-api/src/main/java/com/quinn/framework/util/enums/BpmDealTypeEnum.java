package com.quinn.framework.util.enums;

/**
 * BPM 处理类型（某些场景可多选，支持位存储）
 *
 * @author Qunhua.Liao
 * @since 2020-05-12
 */
public enum BpmDealTypeEnum {

    // 启动
    START(0),

    // 沟通
    FEEDBACK(0),

    // 沟通
    READ(0),

    // 结束
    END(0),

    // 跳过
    SKIP(1),

    // 系统自动
    AUTO(2),

    // 系统专办
    ASSIGN_SYS(2 << 1),

    // 人工同意
    AGREE(2 << 2),

    // 人工拒绝
    REJECT(2 << 3),

    // 人工转办
    ASSIGN(2 << 4),

    // 人工撤回
    REVOKE(2 << 5),

    // 人工终止
    TERMINATE(2 << 6),

    // 抄送
    COPY(2 << 7),

    // 沟通
    COMMUNICATE(2 << 8),

    ;

    /**
     * 编码
     */
    public final int code;

    BpmDealTypeEnum(int code) {
        this.code = code;
    }

}
