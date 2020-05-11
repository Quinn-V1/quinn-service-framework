package com.quinn.framework.controller;

import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 缓存操作
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
@RestController
@RequestMapping("/framework/cache/*")
@Api(tags = {"0ZY050通用：缓存操作"})
public class CacheController extends AbstractController {

    @Resource
    private CacheAllService cacheAllService;

    @GetMapping(value = "get")
    @ApiOperation(value = "获取缓存信息")
    public BaseResult get(
            @ApiParam(name = "key", value = "缓存键", required = true)
            @RequestParam(name = "key") String key
    ) {
        return BaseResult.build(true).ofData(cacheAllService.get(key));
    }

    @DeleteMapping(value = "delete")
    @ApiOperation(value = "删除缓存信息")
    public BaseResult delete(
            @ApiParam(name = "key", value = "缓存键", required = true)
            @RequestParam(name = "key") String key
    ) {
        cacheAllService.delete(key);
        return BaseResult.SUCCESS;
    }

    @PostMapping(value = "set")
    @ApiOperation(value = "设置缓存信息")
    public BaseResult set(
            @ApiParam(name = "key", value = "缓存键", required = true)
            @RequestParam(name = "key") String key,

            @ApiParam(name = "value", value = "缓存值", required = true)
            @RequestParam(name = "value") String value
    ) {
        cacheAllService.set(key, value);
        return BaseResult.SUCCESS;
    }

    @GetMapping(value = "keys")
    @ApiOperation(value = "获取基本缓存自定样式键")
    public BaseResult keys(
            @ApiParam(name = "pattern", value = "缓存键样式")
            @RequestParam(name = "pattern", required = false) String pattern
    ) {
        return BaseResult.build(true).ofData(cacheAllService.keys(pattern));
    }

}
