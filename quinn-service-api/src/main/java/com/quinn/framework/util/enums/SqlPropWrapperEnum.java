package com.quinn.framework.util.enums;

/**
 * 结果字段包装枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-04-24
 */
public enum SqlPropWrapperEnum {

    // 最大值
    MAX,

    // 最小值
    MIN,

    // 求和
    SUM,

    ;

    /**
     * 包装
     *
     * @param filed 字段
     * @return  包装后的字段
     */
    public String wrap(String filed) {
        return name() + "(" + filed + ")";
    }

}
