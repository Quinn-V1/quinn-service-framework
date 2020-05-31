package com.quinn.framework.controller;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.model.DefaultTokenInfo;
import com.quinn.framework.service.SsoService;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    public BaseResult<AuthInfo> login(
            @RequestBody DefaultTokenInfo token
    ) {
        return ssoService.login(token);
    }

    @PostMapping(value = "logout")
    @ApiOperation(value = "注销")
    public BaseResult logout() {
        return ssoService.logout();
    }

    @PostMapping(value = "auth-types")
    @ApiOperation(value = "支持令牌种类")
    public BaseResult<List<StringKeyValue>> authTypes() {
        return ssoService.selectAuthTypes();
    }

    @PostMapping(value = "credentials-matchers")
    @ApiOperation(value = "支持密码校验方式")
    public BaseResult<List<StringKeyValue>> credentialsMatchers() {
        return ssoService.credentialsMatchers();
    }

}
