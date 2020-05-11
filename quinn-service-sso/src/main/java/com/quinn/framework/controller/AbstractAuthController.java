package com.quinn.framework.controller;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.service.AuthService;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 权限验证逻辑转发层
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public abstract class AbstractAuthController<T extends TokenInfo, A extends AuthInfo> {

    @Resource
    private AuthService authService;

    @ResponseBody
    @PostMapping(value = "login")
    @ApiOperation(value = "登录")
    public BaseResult<A> login(
            @RequestBody T token
    ) {
        return authService.login(token);
    }

    @ResponseBody
    @PostMapping(value = "logout")
    @ApiOperation(value = "注销")
    public BaseResult logout() {
        return authService.logout();
    }

}
