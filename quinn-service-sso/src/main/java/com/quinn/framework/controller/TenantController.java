package com.quinn.framework.controller;

import com.quinn.framework.service.SsoService;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 当前用户租户操作接口
 *
 * @author Qunhua.Liao
 * @since 2020-05-23
 */
@RestController
@RequestMapping("/tenant/*")
@Api(tags = {"0ZY091系统：权限租户"})
public class TenantController extends AbstractController {

    @Resource
    private SsoService ssoService;

    @PostMapping(value = "list-mine")
    @ApiOperation(value = "我的租户列表")
    public BaseResult listMine() {
        return ssoService.listMyTenant();
    }

    @PostMapping(value = "set-current")
    @ApiOperation(value = "设置当前租户")
    public BaseResult setCurrent(
            @ApiParam(name = "tenantCode", value = "租户编码", required = true)
            @RequestParam(name = "tenantCode") String tenantCode
    ) {
        return ssoService.setMyCurrentTenant(tenantCode);
    }

}
