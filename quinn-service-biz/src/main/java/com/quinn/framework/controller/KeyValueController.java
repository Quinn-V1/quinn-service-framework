
package com.quinn.framework.controller;

import com.quinn.framework.component.KeyValueMultiService;
import com.quinn.util.base.model.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 通用主数据对外开放接口
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
@RestController
@RequestMapping("/core/key-value/*")
@Api(tags = {"0ZY010数据：通用主数据"})
public class KeyValueController extends AbstractController {

    @PostMapping(value = "list")
    @ApiOperation("获取通用主数据列表")
    public BaseResult generalizedList(
            @ApiParam(name = "condition", value = "Json格式条件", required = true)
            @RequestBody Map<String, Object> condition
    ) {
        return KeyValueMultiService.selectByMap(condition);
    }

}
