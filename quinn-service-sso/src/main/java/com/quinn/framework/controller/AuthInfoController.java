package com.quinn.framework.controller;

import com.quinn.framework.service.SsoService;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 当前用户租户操作接口
 *
 * @author Qunhua.Liao
 * @since 2020-05-23
 */
@RestController
@RequestMapping("/auth-info/*")
@Api(tags = {"0ZY091系统：权限获取"})
public class AuthInfoController extends AbstractController {

    @Resource
    private SsoService ssoService;

    @GetMapping(value = "my-tenants")
    @ApiOperation(value = "我的租户列表")
    public BaseResult myTenants() {
        return ssoService.selectMyTenant();
    }

    @PostMapping(value = "set-tenant")
    @ApiOperation(value = "设置当前租户")
    public BaseResult setCurrent(
            @ApiParam(name = "tenantCode", value = "租户编码", required = true)
            @RequestParam(name = "tenantCode") String tenantCode
    ) {
        return ssoService.setMyCurrentTenant(tenantCode);
    }

    @GetMapping(value = "my-permissions")
    @ApiOperation(value = "我的菜单")
    public BaseResult myPermissions(
            @ApiParam(name = "group", value = "分组编码")
            @RequestParam(name = "group", required = false) String group,

            @ApiParam(name = "type", value = "类型编码", required = true)
            @RequestParam(name = "type") String type,

            @ApiParam(name = "parentId", value = "上级ID")
            @RequestParam(name = "parentId", required = false) Long parentId
    ) {
        return ssoService.selectMyPermissions(group, type, parentId);
    }

}
