package com.quinn.framework.util.enums;

/**
 * Join类型枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-09-28
 */
public enum  JoinTypeEnum {

    // 内关联
    INNER("JOIN"),

    // 左关联
    LEFT("LEFT JOIN"),

    // 右关联
    RIGHT("RIGHT JOIN"),

    // 全关联
    FULL("FULL JOIN"),

    ;

    public final String code;

    JoinTypeEnum(String code) {
        this.code = code;
    }
}
