package com.quinn.framework.model;

import com.quinn.framework.api.AuthInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 模拟权限信息
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
@Setter
@Getter
public class DefaultAuthInfo implements AuthInfo<DefaultAuthInfo> {

    /**
     * 用户编码
     */
    private String principal;

    /**
     * 用户名（可以在多个顶层组织下面：每个组织下面有不同权限）
     */
    private String credentials;

    /**
     * 密码
     */
    private String currentTenantCode;

    /**
     * 角色列表：key为顶层组织；value为当前组织下所有角色
     * <p>
     * 之所以不维护功能列表：是因为功能和角色关联，如果角色变动，来修改这个，影响太大
     */
    private Map<String, DefaultPermission> principals;

    @Override
    public DefaultAuthInfo realInfo() {
        return this;
    }

    @Override
    public void attr(String name, Object value) {
    }
}
