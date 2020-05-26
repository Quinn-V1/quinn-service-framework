package com.quinn.framework.util.constant;

/**
 * 权限相关常量
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface AuthConstant {

    /**
     * 参数名：环境
     */
    String PARAM_NAME_PROFILE = "profile";

    /**
     * 参数名：校验类型
     */
    String PARAM_NAME_AUTH_TYPE = "authType";

    /**
     * 参数名：校验类型
     */
    String PARAM_NAME_FUNCTION_KEY = "functionKey";

    /**
     * 校验类型不支持异常描述
     */
    String ERROR_DESC_AUTH_TYPE_NOT_SUPPORT = "当前运行环境【${" + PARAM_NAME_PROFILE + "}】不支持验证方式【${"
            + PARAM_NAME_AUTH_TYPE + "}】";

    /**
     * 校验类型不支持异常描述
     */
    String ERROR_UNAUTHORIZED_ACCESS = "当前功能需要登录之后才能访问，请登录或者联系管理员注册";

    /**
     * 越权访问
     */
    String ERROR_OVER_AUTHORIZED_ACCESS = "尚未取得功能【${" + PARAM_NAME_FUNCTION_KEY + "}】的访问权限，请联系管理员";

}
