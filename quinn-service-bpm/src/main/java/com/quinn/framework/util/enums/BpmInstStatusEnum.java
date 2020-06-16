package com.quinn.framework.util.enums;

/**
 * 流程状态枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-05-03
 */
public enum BpmInstStatusEnum {

    // 草稿箱
    DRAFT(10),

    // 审批中
    RUNNING(20),

    // 转办
    ASSIGN(21),

    // 驳回
    REJECT(23),

    // 撤回
    REVOKE(25),

    // 待反馈
    WAIT_FEEDBACK(30),

    // 挂起
    SUSPEND(40),

    // 正常结束
    END(50),

    // 手动终结
    TERMINATED(60),

    ;

    /**
     * 数字编码
     */
    public final int code;

    BpmInstStatusEnum(int code) {
        this.code = code;
    }

}
