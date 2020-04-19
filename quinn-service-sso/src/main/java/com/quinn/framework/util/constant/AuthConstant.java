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
     * 校验类型不支持异常描述
     */
    String ERROR_DESC_AUTH_TYPE_NOT_SUPPORT = "当前运行环境【${" + PARAM_NAME_PROFILE + "}】不支持验证方式【${"
            + PARAM_NAME_AUTH_TYPE + "}】";

}
