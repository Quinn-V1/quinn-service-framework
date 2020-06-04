package com.quinn.framework.service;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.LoginPostProcessor;
import com.quinn.framework.api.LoginPrevProcessor;
import com.quinn.framework.model.DefaultTokenInfo;
import com.quinn.util.base.enums.FunctionTypeEnum;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;

import java.util.List;

/**
 * 权限业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface SsoService {

    /**
     * 支持的校验方式
     *
     * @return 校验方式列表
     */
    BaseResult<List<StringKeyValue>> selectAuthTypes();

    /**
     * 登录
     *
     * @param token 令牌信息
     * @return 注销结果（权限信息）
     */
    BaseResult<AuthInfo> login(DefaultTokenInfo token);

    /**
     * 注销
     *
     * @return 注销结果
     */
    BaseResult logout();

    /**
     * 我的租户列表
     *
     * @return 我的租户列表
     */
    BaseResult selectMyTenant();

    /**
     * 设置我的当前租户
     *
     * @param tenantCode 租户编码
     * @return 设置是否成功
     */
    BaseResult setMyCurrentTenant(String tenantCode);

    /**
     * 获取密码校验方式
     *
     * @return 密码校验方式列表
     */
    BaseResult<List<StringKeyValue>> credentialsMatchers();

    /**
     * 添加前置登录增强
     *
     * @param loginPrevProcessors 前置增强器
     */
    default void setLoginPrevProcessors(List<LoginPrevProcessor> loginPrevProcessors) {
    }

    /**
     * 添加后置登录增强
     *
     * @param loginPostProcessors 后置增强器
     */
    default void setLoginPostProcessors(List<LoginPostProcessor> loginPostProcessors) {
    }

    /**
     * 查找我的权限
     *
     * @param group    分组
     * @param type     类型
     * @param parentId 上级权限ID
     * @return 权限列表
     */
    BaseResult selectMyPermissions(String group, FunctionTypeEnum type, Long parentId);

}
