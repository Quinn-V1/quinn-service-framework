package com.quinn.framework.util.enums;

import com.quinn.util.constant.StringConstant;

/**
 * 值字段包装枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-04-24
 */
public enum SqlCondWrapperEnum {

    // 等于
    EQUAL(StringConstant.CHAR_EQUAL_MARK),

    // 不等于
    NOT_EQUAL(StringConstant.NOT_EQUAL_MARK),

    // 小于
    LESS_THAN(StringConstant.CHAR_LESS_THAN),

    // 求和
    LESS_EQUAL(StringConstant.CHAR_LESS_EQUAL),

    // 求和
    GREAT_THAN(StringConstant.CHAR_GREAT_THAN),

    // 求和
    GREAT_EQUAL(StringConstant.CHAR_GREAT_EQUAL),

    // 求和
    LIKE(StringConstant.CHAR_LIKE),

    // 求和
    LIKE_START(StringConstant.CHAR_LIKE),

    ;

    public final String code;

    SqlCondWrapperEnum(String code) {
        this.code = code;
    }

    /**
     * 包装
     *
     * @param filed 字段
     * @return 包装后的字段
     */
    public String wrap(String filed) {
        switch (this) {
            case LIKE:
                return "concat('%', " + filed + ", '%')";
            case LIKE_START:
                return "concat('%', " + filed + ")";
            default:
                return code + filed;
        }
    }

}
