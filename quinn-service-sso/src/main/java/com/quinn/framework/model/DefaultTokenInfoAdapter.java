package com.quinn.framework.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.TokenInfo;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.StringConstant;

/**
 * Shiro 权限对象适配器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class DefaultTokenInfoAdapter implements TokenInfo {

    /**
     * 权限信息
     */
    private JSONObject principal;

    public DefaultTokenInfoAdapter(Object principal) {
        this.principal = (JSONObject) JSON.toJSON(principal);
    }

    @Override
    public String getTokenType() {
        return StringConstant.ALL_OF_DATA;
    }

    @Override
    public BaseResult validate() {
        return principal == null ? BaseResult.fail() : BaseResult.SUCCESS;
    }

    @Override
    public Object getPrincipal() {
        return principal.get("principal");
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
