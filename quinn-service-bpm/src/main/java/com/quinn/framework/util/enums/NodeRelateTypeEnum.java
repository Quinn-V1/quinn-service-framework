package com.quinn.framework.util.enums;

/**
 * 节点关系枚举类
 *
 * @author Simon.z
 * @since 2020-05-25
 */
public enum NodeRelateTypeEnum {

    // 同意
    AGREE("同意", 1),

    // 条件
    CONDITION("条件", 9),

    // 驳回
    REJECT("驳回", 10);

    /**
     * BPM模型类型
     */
    public static final String[] BPM_TYPES = new String[]{CONDITION.name(), AGREE.name()};

    /**
     * 自定义类型
     */
    public static final String[] CUSTOM_TYPES = new String[]{REJECT.name(), CONDITION.name()};

    /**
     * 描述
     */
    public final String desc;

    /**
     * 编码
     */
    public final int code;

    NodeRelateTypeEnum(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
