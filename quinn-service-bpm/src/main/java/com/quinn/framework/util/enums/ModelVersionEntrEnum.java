package com.quinn.framework.util.enums;

/**
 * 模型版本入口枚举类
 *
 * @author Simon.z
 * @since 2020/5/28
 */
public enum ModelVersionEntrEnum {

    // 新建
    NEW("新建", 1),

    // 修改
    MODIFY("修改", 9),

    // 继承
    EXTEND("继承", 10);

    /**
     * 描述
     */
    public final String desc;

    /**
     * 编码
     */
    public final int code;

    ModelVersionEntrEnum(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }

}
