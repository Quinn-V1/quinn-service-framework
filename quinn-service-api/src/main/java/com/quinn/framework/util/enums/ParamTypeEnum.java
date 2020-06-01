package com.quinn.framework.util.enums;

/**
 * 消息参数枚举
 *
 * @author Qunhua.Liao
 * @since 2020-02-08
 */
public enum ParamTypeEnum {

    // JSON
    JSON(10),

    // SQL
    SQL(20),

    // 接口
    HTTP(30),

    ;

    /**
     * 状态编码，具有顺序含义
     */
    public final int code;

    ParamTypeEnum(int code) {
        this.code = code;
    }

}
