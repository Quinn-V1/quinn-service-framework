package com.quinn.framework.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 权限信息
 *
 * @author Qunhua.Liao
 * @since 2020-05-23
 */
@Getter
@Setter
public class DefaultPermission implements Serializable {

    /**
     * 租户编码
     */
    private String key;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 类型：角色
     */
    protected Map<String, Set<String>> rolesMap;

    /**
     * 路径
     */
    protected Map<String, Set<String>> permissionsMap;

    /**
     * 指定类型角色
     *
     * @param roleType 角色类型
     * @return 角色列表
     */
    public Set<String> rolesOf(String roleType) {
        if (rolesMap == null) {
            return null;
        }
        return rolesMap.get(roleType);
    }

    /**
     * 指定类型权限
     *
     * @param functionType 权限类型
     * @return 权限列表
     */
    public Set<String> permissionOf(String functionType) {
        if (permissionsMap == null) {
            return null;
        }
        return permissionsMap.get(functionType);
    }

}
