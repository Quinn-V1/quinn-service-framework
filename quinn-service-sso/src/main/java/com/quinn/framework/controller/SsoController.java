package com.quinn.framework.controller;

import com.quinn.framework.model.DefaultTokenInfo;
import com.quinn.framework.service.SsoService;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 权限验证逻辑转发层
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
@RestController
@RequestMapping("/sso/*")
@Api(tags = {"0ZY090系统：权限认证"})
public class SsoController extends AbstractController {

    @Resource
    private SsoService ssoService;

    @PostMapping(value = "login")
    @ApiOperation(value = "登录")
    public BaseResult login(
            @RequestBody DefaultTokenInfo token
    ) {
        return ssoService.login(token);
    }

    @PostMapping(value = "logout")
    @ApiOperation(value = "注销")
    public BaseResult logout() {
        return ssoService.logout();
    }

}
