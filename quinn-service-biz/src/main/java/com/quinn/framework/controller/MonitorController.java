package com.quinn.framework.controller;

import com.quinn.util.licence.model.ApplicationInfo;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 框架：状态查询
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
@RestController
@RequestMapping("/framework/monitor/*")
@Api(tags = {"0ZZ020框架：系统监控"})
public class MonitorController extends AbstractController {

    /**
     * 应用信息
     */
    @Autowired
    private ApplicationInfo applicationInfo;

    @GetMapping("visit-times")
    @ApiOperation("访问次数")
    BaseResult<String> visitTimes() {
        return BaseResult.build(true).ofData(applicationInfo.getVisitTimes());
    }

    @GetMapping("remain-request")
    @ApiOperation("持有请求数量")
    BaseResult<String> remainRequests() {
        return BaseResult.build(true).ofData(applicationInfo.getRemainRequestCount());
    }

    @GetMapping("session-count")
    @ApiOperation("会话数量")
    BaseResult<String> sessionCount() {
        return BaseResult.build(true).ofData(applicationInfo.getSessionCount());
    }

}
