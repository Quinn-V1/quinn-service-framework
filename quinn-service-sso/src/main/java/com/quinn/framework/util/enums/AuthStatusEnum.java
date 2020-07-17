package com.quinn.framework.util.enums;

/**
 * 权限信息状态
 *
 * @author Qunhua.Liao
 * @since 2020-07-17
 */
public enum AuthStatusEnum {

    CREATE(10),

    NORMAL(30),

    FREEZE(40),

    LOCK(50),

    ;

    /**
     * 状态编码
     */
    public final int code;

    /**
     * 权限信息状态
     *
     * @param code
     */
    AuthStatusEnum(int code) {
        this.code = code;
    }

}
