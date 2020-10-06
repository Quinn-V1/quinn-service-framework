package com.quinn.framework.util.enums;

import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.CharConstant;
import com.quinn.util.constant.StringConstant;
import lombok.SneakyThrows;

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
    @SneakyThrows
    public void wrap(Appendable query, String filed, String alias) {
        switch (this) {
            case LIKE:
                query.append(code).append("concat('%', ");
                if (StringUtil.isNotEmpty(alias)) {
                    query.append(alias).append(CharConstant.DOT);
                }
                query.append(filed).append(", '%')");
            case LIKE_START:
                query.append(code).append("concat(");
                if (StringUtil.isNotEmpty(alias)) {
                    query.append(alias).append(CharConstant.DOT);
                }
                query.append(filed).append(", '%')");
            default:
                query.append(code);
                if (StringUtil.isNotEmpty(alias)) {
                    query.append(alias).append(CharConstant.DOT).append(filed);
                }
                query.append(filed);
        }
    }

}
