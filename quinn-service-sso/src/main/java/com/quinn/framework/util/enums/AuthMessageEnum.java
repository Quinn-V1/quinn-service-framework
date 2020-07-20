package com.quinn.framework.util.enums;

import static com.quinn.framework.util.constant.AuthMessageConstant.*;
import static com.quinn.framework.util.constant.AuthParamName.*;

import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.constant.CommonParamName;
import com.quinn.util.constant.MessageEnumFlag;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.LanguageEnum;

import java.util.Locale;

/**
 * 权限异常枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public enum AuthMessageEnum implements MessageEnumFlag {

    // 验证方式不被支持
    AUTH_TYPE_NOT_SUPPORT(ERROR_DESC_AUTH_TYPE_NOT_SUPPORT, PARAM_NAME_PROFILE,
            PARAM_NAME_AUTH_TYPE),

    // 当前用户未指定租户
    NO_TENANT(DESC_NO_TENANT),

    // 当前用户存在多【${dataSize}】个租户
    MULTI_TENANT(DESC_MULTI_TENANT, CommonParamName.PARAM_DATA_SIZE),

    // 当前用户未分配租户【${tenantCode}】
    ERROR_TENANT(DESC_ERROR_TENANT, PARAM_NAME_TENANT_CODE),

    // 当前功能需要登录之后才能访问，请登录或者联系管理员注册
    UNAUTHORIZED_ACCESS(ERROR_UNAUTHORIZED_ACCESS),

    // 尚未取得功能【${functionKey}】的访问权限，请联系管理员
    OVER_AUTHORIZED_ACCESS(ERROR_OVER_AUTHORIZED_ACCESS, PARAM_NAME_FUNCTION_KEY),

    // 用户名名或密码不匹配
    AUTH_INFO_NOT_FOUND(DESC_AUTH_INFO_NOT_FOUND),

    // 当前时间超出用户有效期
    AUTH_INFO_BEYOND_EFFECT(DESC_AUTH_INFO_BEYOND_EFFECT),

    // 用户状态不匹配
    AUTH_INFO_STATUS_NOT_FIT(DESC_AUTH_INFO_STATUS_NOT_FIT, PARAM_NAME_OF_AUTH_STATUS),

    // 用户名名或密码不匹配
    AUTH_CREDENTIALS_MISMATCH(DESC_AUTH_CREDENTIALS_MISMATCH),

    // 从外部访问内部资源
    INTRANET_RESOURCE_ACCESS_FROM_OUT(DESC_INTRANET_RESOURCE_ACCESS_FROM_OUT),

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

    @Override
    public String key() {
        return StringConstant.DATA_TYPE_OF_MESSAGE + StringConstant.CHAR_COLON + name()
                + StringConstant.CHAR_POUND_SIGN + StringConstant.NONE_OF_DATA;
    }

    static {
        EnumMessageResolver.addContent(Locale.SIMPLIFIED_CHINESE, AuthMessageEnum.values());
    }
}
