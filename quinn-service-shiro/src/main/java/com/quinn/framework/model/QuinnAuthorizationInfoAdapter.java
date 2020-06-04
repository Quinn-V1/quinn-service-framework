package com.quinn.framework.model;

import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.enums.FunctionTypeEnum;
import com.quinn.util.constant.enums.RoleTypeEnum;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;

import java.util.Collection;
import java.util.Set;

/**
 * 授权信息适配器
 *
 * @author Qunhua.Liao
 * @since 2020-05-23
 */
public class QuinnAuthorizationInfoAdapter extends DefaultPermission implements AuthorizationInfo {

    public QuinnAuthorizationInfoAdapter(DefaultPermission srcPermission) {
        setKey(srcPermission.getKey());
        setName(srcPermission.getName());
        setRolesMap(srcPermission.getRolesMap());
        setPermissionsMap(srcPermission.getPermissionsMap());
    }

    @Override
    public Set<String> getRoles() {
        return rolesOf(RoleTypeEnum.FUNCTION.name());
    }

    @Override
    public Collection<String> getStringPermissions() {
        return CollectionUtil.mergeSet(permissionOf(FunctionTypeEnum.INTERFACE.name()),
                permissionOf(FunctionTypeEnum.MATCH.name()));
    }

    @Override
    public Collection<Permission> getObjectPermissions() {
        return null;
    }

}
