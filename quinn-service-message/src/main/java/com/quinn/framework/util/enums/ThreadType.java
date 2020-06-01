package com.quinn.framework.util.enums;

/**
 * 消息线程类别
 *
 * @author Qunhua.Liao
 * @since 2020-02-12
 */
public enum ThreadType {

    // 参数线程
    PARAM(10),

    // 内容线程
    CONTENT(20),

    // 收件对象线程
    RECEIVER(30),

    ;

    public final int code;

    ThreadType(int code) {
        this.code = code;
    }

}
