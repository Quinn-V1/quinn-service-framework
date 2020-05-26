package com.quinn.framework.util.enums;

import com.quinn.framework.util.constant.AuthConstant;
import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.constant.MessageEnumFlag;
import com.quinn.util.constant.enums.LanguageEnum;

/**
 * 权限异常枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public enum AuthExceptionEnum implements MessageEnumFlag {

    // 验证方式不被支持
    AUTH_TYPE_NOT_SUPPORT(AuthConstant.ERROR_DESC_AUTH_TYPE_NOT_SUPPORT, AuthConstant.PARAM_NAME_PROFILE,
            AuthConstant.PARAM_NAME_AUTH_TYPE),

    // 未授权访问
    UNAUTHORIZED_ACCESS(AuthConstant.ERROR_UNAUTHORIZED_ACCESS),

    // 未授权访问
    OVER_AUTHORIZED_ACCESS(AuthConstant.ERROR_OVER_AUTHORIZED_ACCESS, AuthConstant.PARAM_NAME_FUNCTION_KEY),

    ;

    /**
     * 默认描述
     */
    public final String defaultDesc;

    /**
     * 参数名称
     */
    public final String[] paramNames;

    /**
     * 构造器
     *
     * @param defaultDesc 默认描述
     */
    AuthExceptionEnum(String defaultDesc, String... paramNames) {
        this.defaultDesc = defaultDesc;
        this.paramNames = paramNames;
    }

    @Override
    public String defaultDesc() {
        return defaultDesc;
    }

    @Override
    public String[] paramNames() {
        return paramNames;
    }

    static {
        EnumMessageResolver.addContent(LanguageEnum.zh_CN.locale, AuthExceptionEnum.values());
    }
}
