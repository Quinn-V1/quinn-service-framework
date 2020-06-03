package com.quinn.framework.util.enums;

import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.constant.MessageEnumFlag;
import com.quinn.util.constant.StringConstant;

import java.util.Locale;

/**
 * 消息类型枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-02-09
 */
public enum MessageTypeEnum implements MessageEnumFlag {

    // 邮件
    EMAIL("邮件"),

    // 微信
    WE_CHART("微信"),

    // 短信
    MOBILE("短信"),

    // 站内信
    WEB("站内信"),

    // APP
    APP("APP");

    public final String defaultDesc;

    MessageTypeEnum(String defaultDesc) {
        this.defaultDesc = defaultDesc;
    }

    @Override
    public String defaultDesc() {
        return defaultDesc;
    }

    @Override
    public String[] paramNames() {
        return null;
    }

    static {
        EnumMessageResolver.addContent(Locale.SIMPLIFIED_CHINESE, MessageTypeEnum.values());
    }

    /**
     * 默认的次级主键
     *
     * @param type 消息类型
     * @return 次级主键（参照MessageServer）
     */
    public static final String defaultSubKey(String type) {
        return type + StringConstant.CHAR_COLON + StringConstant.ALL_OF_DATA;
    }

}
