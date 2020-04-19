package com.quinn.framework.util.enums;

import com.quinn.framework.util.constant.AuthConstant;

/**
 * 权限异常枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public enum AuthExceptionEnum {

    // 验证方式不被支持
    AUTH_TYPE_NOT_SUPPORT(AuthConstant.ERROR_DESC_AUTH_TYPE_NOT_SUPPORT, AuthConstant.PARAM_NAME_PROFILE,
            AuthConstant.PARAM_NAME_AUTH_TYPE),

    ;

    /**
     * 默认描述
     */
    public final String defaultDesc;

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

}
