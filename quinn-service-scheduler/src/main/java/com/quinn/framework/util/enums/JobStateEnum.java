package com.quinn.framework.util.enums;

import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;

/**
 * 任务同步类型
 *
 * @author Qunhua.Liao
 * @since 2020-02-15
 */
public enum JobStateEnum {

    // 待执行
    TODO(10),

    // 执行失败
    DONE_FAIL(20),

    // 执行中
    DOING(30),

    // 执行成功
    DONE_SUCCESS(40),

    ;

    /**
     * 状态编码，具有顺序含义
     */
    public final int code;

    JobStateEnum(int code) {
        this.code = code;
    }

    /**
     * 编码转为名称
     *
     * @param code 编码
     * @return 名称
     */
    public static String codeToName(int code) {
        for (JobStateEnum e : values()) {
            if (code == e.code) {
                return e.name();
            }
        }
        return StringConstant.NONE_OF_DATA;
    }

    /**
     * 名称转换为编码
     *
     * @param name 名称
     * @return 编码
     */
    public static int nameToCode(String name) {
        for (JobStateEnum e : values()) {
            if (e.name().equals(name)) {
                return e.code;
            }
        }
        return NumberConstant.NONE_OF_DATA_INT;
    }

}
