package com.quinn.framework.model;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.util.enums.TokenTypeEnum;
import com.quinn.util.base.model.BaseResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 模拟令牌
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
@Setter
@Getter
public class DefaultTokenInfo implements TokenInfo {

    public DefaultTokenInfo() {
    }

    public DefaultTokenInfo(String principal, String credentials) {
        this(TokenTypeEnum.DB_USER.name(), principal, credentials);
    }

    public DefaultTokenInfo(String tokenType, String principal, String credentials) {
        this.tokenType = tokenType;
        this.principal = principal;
        this.credentials = credentials;
    }

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * 令牌明文(用户名)用户名
     */
    private String principal;

    /**
     * 密码
     */
    private String credentials;

    /**
     * 附加信息
     */
    private JSONObject extraProps;

    @Override
    public BaseResult validate() {
        return BaseResult.SUCCESS;
    }

    @Override
    public Object attr(String name) {
        return extraProps == null ? null : extraProps.get(name);
    }

}
