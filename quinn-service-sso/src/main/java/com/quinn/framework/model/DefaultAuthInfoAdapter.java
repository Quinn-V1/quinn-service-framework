package com.quinn.framework.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.AuthInfo;
import com.quinn.util.constant.ConfigConstant;

import java.util.Map;

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
    private JSONObject extraProps;

    public DefaultAuthInfoAdapter(Object principal) {
        this.extraProps = (JSONObject) JSON.toJSON(principal);
    }

    @Override
    public JSONObject realInfo() {
        return extraProps;
    }

    @Override
    public void ofRealInfo(JSONObject jsonObject) {
        this.extraProps = jsonObject;
    }

    @Override
    public Object getPrincipal() {
        return extraProps.get(ConfigConstant.DEFAULT_PRINCIPAL_ID_FIELD_NAME);
    }

    @Override
    public Object getPrincipals() {
        return extraProps.get("principals");
    }

    @Override
    public Object getCredentials() {
        return extraProps.get("credentials");
    }

    @Override
    public String getCurrentTenantCode() {
        return extraProps.getString("currentTenantCode");
    }

    @Override
    public void setCurrentTenantCode(String tenantCode) {
        extraProps.put("currentTenantCode", tenantCode);
    }

    @Override
    public Object attr(String name) {
        return extraProps.get(name);
    }

    @Override
    public void attr(String name, Object value) {
        if (extraProps == null) {
            extraProps = new JSONObject();
        }
        extraProps.put(name, value);
    }

    @Override
    public Map<String, Object> getExtraProps() {
        return extraProps;
    }
}
