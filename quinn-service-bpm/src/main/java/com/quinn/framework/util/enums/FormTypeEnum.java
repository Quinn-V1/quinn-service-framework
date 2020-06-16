package com.quinn.framework.util.enums;

/**
 * 表单类型枚举类
 *
 * @author Simon.z
 * @since 2020-05-25
 */
public enum FormTypeEnum {

    // 外部表单类型
    EXTERNAL_FORM("外部表单", 1),

    // 内部表单类型
    INTERNAL_FORM("内部表单", 2),

    ;

    /**
     * 描述
     */
    public final String desc;

    /**
     * 编码
     */
    public final int code;

    FormTypeEnum(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
