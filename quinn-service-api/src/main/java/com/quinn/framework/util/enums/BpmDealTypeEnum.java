package com.quinn.framework.util.enums;

/**
 * BPM 处理类型（某些场景可多选，支持位存储）
 *
 * @author Qunhua.Liao
 * @since 2020-05-12
 */
public enum BpmDealTypeEnum {

    // 启动
    START(-16),

    // 跳过
    SKIP(-8),

    // 系统自动
    AUTO(-4),

    // 结束
    END(-2),

    // 系统转办
    ASSIGN_SYS(-1),

    // 反馈
    FEEDBACK(0),

    // 沟通
    READ(0),

    // 人工同意
    AGREE(0),

    // 人工拒绝
    REJECT(1),

    // 人工转办
    ASSIGN(2),

    // 人工撤回
    REVOKE(4),

    // 人工终止
    TERMINATE(8),

    // 抄送
    COPY(16),

    // 沟通
    COMMUNICATE(32),

    ;

    /**
     * 编码
     */
    public final int code;

    BpmDealTypeEnum(int code) {
        this.code = code;
    }

    /**
     * 是否支持本操作
     *
     * @return 是否支持
     */
    public boolean accept(Integer dealTypes) {
        if (code <= 0) {
            return true;
        }
        return (code & dealTypes) > 0;
    }

    /**
     * 处理类型是否被支持
     *
     * @param dealTypeCodes 处理类型（选项位存储）
     * @param dealType      处理类型
     * @return 是否支持
     */
    public static boolean dealTypeSupport(Integer dealTypeCodes, String dealType) {
        int code = BpmDealTypeEnum.valueOf(dealType).code;
        return (dealTypeCodes & code) > 0;
    }

}
