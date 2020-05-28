package com.quinn.framework.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.AuthInfo;
import com.quinn.util.base.constant.ConfigConstant;

/**
 * Shiro 权限对象适配器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class DefaultAuthInfoAdapter implements AuthInfo<JSONObject> {

    /**
     * 权限信息
     */
    private JSONObject realInfo;

    public DefaultAuthInfoAdapter(Object principal) {
        this.realInfo = (JSONObject) JSON.toJSON(principal);
    }

    @Override
    public JSONObject realInfo() {
        return realInfo;
    }

    @Override
    public void ofRealInfo(JSONObject jsonObject) {
        this.realInfo = jsonObject;
    }

    @Override
    public Object getPrincipal() {
        return realInfo.get(ConfigConstant.DEFAULT_PRINCIPAL_ID_FIELD_NAME);
    }

    @Override
    public Object getPrincipals() {
        return realInfo.get("principals");
    }

    @Override
    public Object getCredentials() {
        return realInfo.get("credentials");
    }

    @Override
    public String getCurrentTenantCode() {
        return realInfo.getString("currentTenantCode");
    }

    @Override
    public void setCurrentTenantCode(String tenantCode) {
        realInfo.put("currentTenantCode", tenantCode);
    }

    @Override
    public Object attr(String name) {
        return realInfo.get(name);
    }

}
