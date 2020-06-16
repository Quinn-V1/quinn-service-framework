package com.quinn.framework.util.enums;

/**
 * 占用状态枚举
 *
 * @author Qunhua.Liao
 * @since 2020-05-17
 */
public enum ClaimStatusEnum {

    // 未占用
    EMPTY(-1),

    // 我占用
    MY_CLAIM(10),

    // 别人占用
    OTHER_CLAIM(90),
    ;

    /**
     * 状态数字编码
     */
    public final int code;

    ClaimStatusEnum(int code) {
        this.code = code;
    }

}
