package com.quinn.framework.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.DynamicFilter;
import com.quinn.framework.exception.AuthInfoNotFoundException;
import com.quinn.framework.exception.UnauthorizedException;
import com.quinn.framework.model.DefaultPermission;
import com.quinn.framework.model.DefaultTokenInfo;
import com.quinn.framework.util.*;
import com.quinn.framework.util.enums.AuthMessageEnum;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.util.Base64Utils;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base64 基础权限校验
 *
 * @author Qunhua.Liao
 * @since 2020-06-19
 */
public class PreemptiveBasicAuthFilter implements DynamicFilter {

    /**
     * 默认用户类型
     */
    private static final String DEFAULT_TOKEN_TYPE = "DB_USER";

    /**
     * 权限验证信息
     */
    private static final String AUTH_HEADER_KEY = "Authorization";

    /**
     * 正式帐号信息
     */
    private static final String AUTH_REAL_PRINCIPAL = "RealPrincipal";

    @Override
    public String name() {
        return "basic";
    }

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!StringUtil.isEmptyInFrame(SessionUtil.getUserKey())) {
            filterChain.doFilter(request, response);
            return;
        }

        String auth = request.getHeader(AUTH_HEADER_KEY);
        if (StringUtil.isEmpty(auth)) {
            MultiErrorHandler.handleError(new UnauthorizedException().ofStatusCode(HttpStatus.UNAUTHORIZED.value())
                    .buildParam(AuthMessageEnum.UNAUTHORIZED_ACCESS.key(), 0, 0)
                    .exception(), request, response);
            return;
        }

        auth = auth.substring(Base64Util.BASIC_PREFIX.length());
        String authString = StringUtil.forBytes(Base64Utils.decodeFromString(auth));

        // TODO 以此为缓存，优先查找缓存
        auth = request.getHeader(AUTH_REAL_PRINCIPAL);

        String[] userAndPass = authString.split(StringConstant.CHAR_COLON);
        if (userAndPass.length < NumberConstant.INT_TWO) {
            MultiErrorHandler.handleError(new AuthInfoNotFoundException(), request, response);
        }

        DefaultTokenInfo tokenInfo = new DefaultTokenInfo();
        if (userAndPass.length == NumberConstant.INT_TWO) {
            tokenInfo.setTokenType(DEFAULT_TOKEN_TYPE);
            tokenInfo.setPrincipal(userAndPass[0]);
            tokenInfo.setCredentials(userAndPass[1]);
        } else if (userAndPass.length == NumberConstant.INT_THREE) {
            tokenInfo.setTokenType(userAndPass[0]);
            tokenInfo.setPrincipal(userAndPass[1]);
            tokenInfo.setCredentials(userAndPass[2]);
        } else {
            tokenInfo.setTokenType(userAndPass[0]);
            tokenInfo.setPrincipal(userAndPass[1]);
            tokenInfo.setTenantCode(userAndPass[2]);
            tokenInfo.setCredentials(userAndPass[userAndPass.length - 1]);
        }

        BaseResult<AuthInfo> authInfoRes = MultiAuthInfoFetcher.fetchInfo(tokenInfo);
        if (!authInfoRes.isSuccess()) {
            MultiErrorHandler.handleError(new AuthInfoNotFoundException(), request, response);
        }

        boolean match = MultiCredentialsMatcher.doCredentialsMatch(tokenInfo, authInfoRes.getData());
        if (!match) {
            MultiErrorHandler.handleError(new AuthInfoNotFoundException(), request, response);
        }

        AuthInfo authInfo = authInfoRes.getData();
        if (!StringUtil.isEmpty(auth)) {
            userAndPass = auth.split(StringConstant.CHAR_COLON);
            if (userAndPass.length > 1) {
                tokenInfo.setTokenType(userAndPass[0]);
                tokenInfo.setPrincipal(userAndPass[1]);
            } else {
                tokenInfo.setPrincipal(userAndPass[0]);
            }
            authInfoRes = MultiAuthInfoFetcher.fetchInfo(tokenInfo);
            if (authInfoRes.isSuccess()) {
                authInfo = authInfoRes.getData();
            }
        }

        SessionUtil.setValue(SessionUtil.SESSION_KEY_USER_ID, authInfo.attr(AuthInfo.ATTR_NAME_ID));
        SessionUtil.setValue(SessionUtil.SESSION_KEY_USER_KEY, BaseConverter.staticToString(authInfo.getPrincipal()));
        SessionUtil.setValue(SessionUtil.SESSION_KEY_ORG_KEY, authInfo.getCurrentTenantCode());

        DefaultPermission permission = MultiAuthInfoFetcher.fetchPermissions(authInfo);
        SessionUtil.setAuthInfo((JSONObject) JSON.toJSON(authInfo));
        SessionUtil.setPermissions(permission.getPermissionsMap());
        SessionUtil.setRoles(permission.getRolesMap());

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
