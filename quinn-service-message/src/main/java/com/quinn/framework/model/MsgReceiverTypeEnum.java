package com.quinn.framework.model;

/**
 * 消息接受对象类型枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-02-10
 */
public enum MsgReceiverTypeEnum {

    // 角色
    ROLE(""),

    // 用户
    USER(""),

    // 直接
    DIRECT(""),

    // SQL
    SQL(""),

    // 参数
    PARAM(""),

    ;

    public final String defaultDesc;

    MsgReceiverTypeEnum(String defaultDesc) {
        this.defaultDesc = defaultDesc;
    }

}
