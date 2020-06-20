package com.quinn.framework.util.enums;

/**
 * 表单位置枚举类
 *
 * @author Simon.z
 * @since 2020-05-25
 */
public enum FormPositionEnum {

    // 申请表单
    APPLICATION_FORM("申请表单", 1),

    // 审批表单
    APPROVAL_FORM("审批表单", 2),

    // 移动表单
    MOBILE_FORM("移动表单", 10),

    // 移动审批
    MOBILE_APPROVAL("移动审批", 11);

    /**
     * 描述
     */
    public final String desc;

    /**
     * 编码
     */
    public final int code;

    FormPositionEnum(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
