package com.quinn.framework.util.enums;

import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.constant.MessageEnumFlag;

import java.util.Locale;

/**
 * 令牌类型枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public enum AuthTypeEnum implements MessageEnumFlag {

    // 数据库用户
    DB_USER("数据库用户"),

    // 模拟用户
    MOCK_USER("模拟用户"),

    // 微信用户
    WE_CHAT_USER("微信用户"),

    // 手机用户
    MOBILE_USER("手机用户"),

    // 邮件用户
    EMAIL_USER("邮件用户"),

    // 外部系统用户
    SSO_USER("外部系统用户"),

    ;

    private String defaultDesc;

    AuthTypeEnum(String defaultDesc) {
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
        EnumMessageResolver.addContent(Locale.SIMPLIFIED_CHINESE, AuthTypeEnum.values());
    }

}
