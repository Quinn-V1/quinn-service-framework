package com.quinn.framework.util.enums;

import com.quinn.framework.util.constant.AuthMessageConstant;
import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.constant.CommonParamName;
import com.quinn.util.constant.MessageEnumFlag;
import com.quinn.util.constant.enums.LanguageEnum;

/**
 * 权限异常枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public enum AuthMessageEnum implements MessageEnumFlag {

    // 验证方式不被支持
    AUTH_TYPE_NOT_SUPPORT(AuthMessageConstant.ERROR_DESC_AUTH_TYPE_NOT_SUPPORT, AuthMessageConstant.PARAM_NAME_PROFILE,
            AuthMessageConstant.PARAM_NAME_AUTH_TYPE),

    // 当前用户未指定租户
    NO_TENANT(AuthMessageConstant.DESC_NO_TENANT),

    // 当前用户存在多【${dataSize}】个租户
    MULTI_TENANT(AuthMessageConstant.DESC_MULTI_TENANT, CommonParamName.PARAM_DATA_SIZE),

    // 当前功能需要登录之后才能访问，请登录或者联系管理员注册
    UNAUTHORIZED_ACCESS(AuthMessageConstant.ERROR_UNAUTHORIZED_ACCESS),

    // 尚未取得功能【${functionKey}】的访问权限，请联系管理员
    OVER_AUTHORIZED_ACCESS(AuthMessageConstant.ERROR_OVER_AUTHORIZED_ACCESS, AuthMessageConstant.PARAM_NAME_FUNCTION_KEY),

    // 用户名名或密码不匹配
    AUTH_INFO_NOT_FOUND(AuthMessageConstant.DESC_AUTH_INFO_NOT_FOUND);

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
    AuthMessageEnum(String defaultDesc, String... paramNames) {
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
        EnumMessageResolver.addContent(LanguageEnum.zh_CN.locale, AuthMessageEnum.values());
    }
}
