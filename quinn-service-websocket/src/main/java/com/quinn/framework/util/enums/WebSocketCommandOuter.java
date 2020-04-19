package com.quinn.framework.util.enums;

/**
 * Websocket写出命令
 *
 * @author Qunhua.Liao
 * @since 2020-02-21
 */
public enum WebSocketCommandOuter {

    // 通知
    NOTIFY(-10),

    // 消息
    MESSAGE(-20),

    // 国际化
    I18N(-30),

    // 心跳
    BEAT(-80),

    // 踢出登录
    KICK_OUT(-90)
    ;

    /**
     * 状态编码，具有顺序含义
     */
    public final int code;

    WebSocketCommandOuter(int code) {
        this.code = code;
    }

    /**
     * 编码转为名称
     *
     * @param code 编码
     * @return 名称
     */
    public static String codeToName(int code) {
        for (WebSocketCommandOuter e : values()) {
            if (code == e.code) {
                return e.name();
            }
        }
        return MESSAGE.name();
    }

    /**
     * 名称转换为编码
     *
     * @param name 名称
     * @return 编码
     */
    public static int nameToCode(String name) {
        for (WebSocketCommandOuter e : values()) {
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
    public static WebSocketCommandOuter codeToEnum(Integer syncType) {
        if (syncType == null) {
            return MESSAGE;
        }
        for (WebSocketCommandOuter e : values()) {
            if (e.code == syncType.intValue()) {
                return e;
            }
        }
        return MESSAGE;
    }

}
