package com.quinn.framework.util.enums;

/**
 * Callable类型
 *
 * @author Qunhua.Liao
 * @since 2020-04-05
 */
public enum CallableParameterDirectionEnum {

    // 入参
    IN(1),

    // 出参
    OUT(2),

    // 双向
    BOTH(3)

    ;

    /**
     * 类型值
     */
    public final int code;

    CallableParameterDirectionEnum(int code) {
        this.code = code;
    }

}
