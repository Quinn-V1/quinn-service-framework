package com.quinn.framework.model;

import com.quinn.framework.api.AuthInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

/**
 * 模拟权限信息
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
@Setter
@Getter
public class MockAuthInfo implements AuthInfo {

    /**
     * 用户名（可以在多个顶层组织下面：每个组织下面有不同权限）
     */
    private String username;

    /**
     * 角色列表：key为顶层组织；value为当前组织下所有角色
     *
     * 之所以不维护功能列表：是因为功能和角色关联，如果角色变动，来修改这个，影响太大
     */
    private Map<String, Set<String>> roles;

    @Override
    public Object getPrincipals() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return username;
    }

}
