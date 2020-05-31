package com.quinn.framework.util.enums;

import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.constant.MessageEnumFlag;

import java.util.Locale;

/**
 * 密码校验类型枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public enum CmTypeEnum implements MessageEnumFlag {

    // 数据库用户
    md5CredentialsSubMatcher("MD5普通加密"),

    ;

    private String defaultDesc;

    CmTypeEnum(String defaultDesc) {
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
        EnumMessageResolver.addContent(Locale.SIMPLIFIED_CHINESE, CmTypeEnum.values());
    }
}
