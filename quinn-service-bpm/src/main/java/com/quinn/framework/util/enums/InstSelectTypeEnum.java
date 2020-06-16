package com.quinn.framework.util.enums;

/**
 * 流程选择类型
 *
 * @author Qunhua.Liao
 * @since 2020-05-03
 */
public enum InstSelectTypeEnum {

    // 申请
    APPLY(1),

    // 待办
    TODO(2),

    // 已办
    DONE(4),

    // 抄送
    COPY(8),

    // 全部
    ALL(15),

    ;

    /**
     * 数字编码
     */
    public final int code;

    InstSelectTypeEnum(int code) {
        this.code = code;
    }

}
