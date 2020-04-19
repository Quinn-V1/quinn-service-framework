package com.quinn.framework.util.enums;

/**
 * Callable类型
 *
 * @author Qunhua.Liao
 * @since 2020-04-05
 */
public enum CallableTypeEnum {

    // 存储过程
    PROCEDURE(1),

    // 函数
    FUNCTION(2)

    ;

    /**
     * 类型值
     */
    public final int code;

    CallableTypeEnum(int code) {
        this.code = code;
    }

}
