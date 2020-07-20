package com.quinn.framework.util.constant;

import com.quinn.util.constant.CommonParamName;
import static com.quinn.framework.util.constant.AuthParamName.*;

/**
 * 权限相关常量
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface AuthMessageConstant {

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

    /**
     * 当前用户未分配租户
     */
    String DESC_NO_TENANT = "当前用户未分配租户";

    /**
     * 当前用户未分配租户【${tenantCode}】
     */
    String DESC_ERROR_TENANT = "当前用户未分配租户【${" + PARAM_NAME_TENANT_CODE + "}】";

    /**
     * 当前用户存在多【${dataSize}】个租户
     */
    String DESC_MULTI_TENANT = "当前用户存在多【${" + CommonParamName.PARAM_DATA_SIZE + "}】个租户";

    /**
     * 用户名或密码不匹配
     */
    String DESC_AUTH_INFO_NOT_FOUND = "用户名、密码或者租户不匹配";

    /**
     * 当前时间超出用户有效期
     */
    String DESC_AUTH_INFO_BEYOND_EFFECT = "当前时间超出用户有效期";

    /**
     * 用户状态不能登录系统
     */
    String DESC_AUTH_INFO_STATUS_NOT_FIT = "状态为【${" + PARAM_NAME_OF_AUTH_STATUS  + "}】的用户不可登录系统";

    /**
     * 用户名或密码不匹配
     */
    String DESC_AUTH_CREDENTIALS_MISMATCH = "用户名、密码或者租户不匹配";

    /**
     * 用户名或密码不匹配
     */
    String DESC_INTRANET_RESOURCE_ACCESS_FROM_OUT = "框架资源只能从内网访问, 请使用\"127.0.0.1\"替代\"localhost\"";

}
