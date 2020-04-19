package com.quinn.framework.util.enums;

/**
 * Websocket 写入命令
 *
 * @author Qunhua.Liao
 * @since 2020-02-21
 */
public enum WebSocketCommandInner {

    // 通知
    NOTIFY(10)
    ;

    /**
     * 状态编码，具有顺序含义
     */
    public final int code;

    WebSocketCommandInner(int code) {
        this.code = code;
    }

    /**
     * 编码转为名称
     *
     * @param code 编码
     * @return 名称
     */
    public static String codeToName(int code) {
        for (WebSocketCommandInner e : values()) {
            if (code == e.code) {
                return e.name();
            }
        }
        return NOTIFY.name();
    }

    /**
     * 名称转换为编码
     *
     * @param name 名称
     * @return 编码
     */
    public static int nameToCode(String name) {
        for (WebSocketCommandInner e : values()) {
            if (e.name().equals(name)) {
                return e.code;
            }
        }
        return 10;
    }

    /**
     * 通过代码获取枚举对象
     *
     * @param syncType 类型代码
     * @return 枚举对象
     */
    public static WebSocketCommandInner codeToEnum(Integer syncType) {
        if (syncType == null) {
            return NOTIFY;
        }
        for (WebSocketCommandInner e : values()) {
            if (e.code == syncType.intValue()) {
                return e;
            }
        }
        return NOTIFY;
    }

}
