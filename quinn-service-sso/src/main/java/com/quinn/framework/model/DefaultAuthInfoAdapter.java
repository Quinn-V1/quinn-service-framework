package com.quinn.framework.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.AuthInfo;

/**
 * Shiro 权限对象适配器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class DefaultAuthInfoAdapter implements AuthInfo {

    /**
     * 权限信息
     */
    private JSONObject principal;

    public DefaultAuthInfoAdapter(Object principal) {
        this.principal = (JSONObject) JSON.toJSON(principal);
    }

    @Override
    public Object getPrincipals() {
        return principal.get("principals");
    }

    @Override
    public Object getCredentials() {
        return principal.get("credentials");
    }

    @Override
    public Object attr(String name) {
        return principal.get(name);
    }

}
