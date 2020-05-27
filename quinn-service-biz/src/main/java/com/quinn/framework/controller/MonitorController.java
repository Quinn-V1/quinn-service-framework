package com.quinn.framework.controller;

import com.quinn.util.base.model.BaseResult;
import com.quinn.util.licence.model.ApplicationInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * 框架：状态查询
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
@RestController
@RequestMapping("/core/monitor/*")
@Api(tags = {"0ZZ020框架：系统监控"})
public class MonitorController extends AbstractController {

    /**
     * 应用信息
     */
    @Resource
    private ApplicationInfo applicationInfo;

    @GetMapping("visit-times")
    @ApiOperation("访问次数")
    BaseResult<LongAdder> visitTimes() {
        return BaseResult.success(applicationInfo.getVisitTimes());
    }

    @GetMapping("remain-request")
    @ApiOperation("持有请求数量")
    BaseResult<AtomicInteger> remainRequests() {
        return BaseResult.success(applicationInfo.getRemainRequestCount());
    }

    @GetMapping("session-count")
    @ApiOperation("会话数量")
    BaseResult<AtomicInteger> sessionCount() {
        return BaseResult.success(applicationInfo.getSessionCount());
    }

}
