package com.quinn.framework.util.enums;

/**
 * SQL拼接方式枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-09-28
 */
public enum AppendWayEnum {

    // WHERE条件
    WHERE("WHERE"),

    // JOIN条件
    ON("ON"),

    // 追加条件
    AND("AND"),

    // 追加条件
    UPDATE("UPDATE"),

    // 追加条件
    SET("SET"),

    // 追加条件
    INSERT("INSERT INTO"),

    // 追加条件
    GROUP_BY("GROUP BY"),

    // 追加条件
    ORDER_BY("ORDER BY"),

    // 追加条件
    SELECT("SELECT"),

    // 追加条件
    FROM("FROM"),

    // 追加条件
    IS_NULL("IS NULL"),

    // 追加条件
    NULL("NULL"),

    // 追加条件
    VALUES("VALUES"),

    ;

    public final String code;

    AppendWayEnum(String code) {
        this.code = code;
    }

}
