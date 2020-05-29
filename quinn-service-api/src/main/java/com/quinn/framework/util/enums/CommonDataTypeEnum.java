package com.quinn.framework.util.enums;

/**
 * 通用数据类型枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-05-29
 */
public enum CommonDataTypeEnum {

    // 租户
    TENANT("OrgInfoVO", "租户"),

    ;

    /**
     * 实际编码（对应数据库实体）
     */
    public final String code;

    /**
     * 默认描述
     */
    public final String defaultDesc;

    CommonDataTypeEnum(String code, String defaultDesc) {
        this.code = code;
        this.defaultDesc = defaultDesc;
    }

}
