
package com.quinn.framework.controller;

import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 系统对外开放接口
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
@RestController
@RequestMapping("/framework/status/*")
@Api(tags = {"0ZZ010框架：状态查询"})
public class StatusRestfulController extends AbstractController {

    @GetMapping("touch-result")
    @ApiOperation("查看应用是否正常启动")
    BaseResult<String> touchRestFul(
            @ApiParam(name = "param", value = "参数", required = true)
            @RequestParam(name = "param") String param
    ) {
        return BaseResult.build(true).ofData(param);
    }

    @GetMapping(value = "now-result")
    @ApiOperation(value = "查询当前时间")
    public BaseResult<String> nowRestFul(
    ) {
        return BaseResult.build(true).ofData(BaseConverter.staticToString(LocalDateTime.now()));
    }

}
