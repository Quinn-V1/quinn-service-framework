package com.quinn.framework.controller;

import com.quinn.framework.entity.data.IdGenerateAbleDO;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.model.BatchUpdateInfo;
import com.quinn.framework.model.PageInfo;
import com.quinn.framework.service.BaseEntityService;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实体操作逻辑跳转层t通用基础类
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class BaseEntityController<DO extends IdGenerateAbleDO, TO extends BaseDTO, VO extends DO>
        extends AbstractController {

    /**
     * 基础实体业务操作对象
     */
    private BaseEntityService<DO, TO, VO> baseEntityService;

    /**
     * 实体类型
     */
    private Class<DO> DOClass;

    /**
     * 条件类型
     */
    private Class<TO> TOClass;

    /**
     * 展示类型
     */
    private Class<VO> VOClass;

    public BaseEntityController(BaseEntityService<DO, TO, VO> baseEntityService) {
        this.baseEntityService = baseEntityService;
        DOClass = baseEntityService.getDOClass();
        TOClass = baseEntityService.getTOClass();
        VOClass = baseEntityService.getVOClass();
    }

    @PostMapping(value = "insert")
    @ApiOperation(value = "保存数据")
    public BaseResult<VO> insert(
            @ApiParam(name = "data", value = "Json格式数据", required = true)
            @RequestBody VO data
    ) {
        return baseEntityService.insert(data.prepareForInsert(SessionUtil.getUserKey(), SessionUtil.getOrgKey()));
    }

    @DeleteMapping(value = "delete")
    @ApiOperation(value = "删除数据")
    public BaseResult<VO> delete(
            @ApiParam(name = "data", value = "Json格式数据", required = true)
            @RequestBody VO data
    ) {
        return baseEntityService.delete(data.prepareForDelete(SessionUtil.getUserKey(), false));
    }

    @PutMapping(value = "update")
    @ApiOperation("更新数据")
    public BaseResult<VO> update(
            @ApiParam(name = "data", value = "Json格式数据", required = true)
            @RequestBody VO data
    ) {
        return baseEntityService.update(data.prepareForUpdate(SessionUtil.getUserKey(), false));
    }

    @DeleteMapping(value = "delete-list")
    @ApiOperation(value = "批量删除")
    public BatchResult<VO> deleteList(
            @ApiParam(name = "data", value = "数据列表，含ID", required = true)
            @RequestBody List<VO> dataList
    ) {
        return baseEntityService.deleteList(dataList, true, false);
    }

    @PutMapping(value = "update-batch")
    @ApiOperation("批量更新")
    public BatchResult<VO> updateBatch(
            @ApiParam(name = "data", value = "新数据列表 & 旧数据列表", required = true)
            @RequestBody BatchUpdateInfo<VO> dataList
    ) {
        return baseEntityService.updateBatch(dataList, true, false, false);
    }

    @GetMapping(value = "get-by-id")
    @ApiOperation("获取指定数据")
    public BaseResult<VO> getById(
            @ApiParam(name = "id", value = "系统主键", required = true)
            @RequestParam(name = "id") Long id
    ) {
        return baseEntityService.getById(id);
    }

    @PostMapping(value = "get")
    @ApiOperation("获取指定数据")
    public BaseResult<VO> get(
            @ApiParam(name = "condition", value = "Json格式条件", required = true)
            @RequestBody TO condition
    ) {
        return baseEntityService.get(condition);
    }

    @PostMapping(value = "select")
    @ApiOperation("获取数据列表")
    public BaseResult<List<VO>> select(
            @ApiParam(name = "condition", value = "Json格式条件", required = true)
            @RequestBody TO condition
    ) {
        return baseEntityService.select(condition);
    }

    @PostMapping(value = "page")
    @ApiOperation("分页获取")
    public BaseResult<PageInfo<VO>> page(
            @ApiParam(name = "condition", value = "Json格式条件", required = true)
            @RequestBody TO condition
    ) {
        return baseEntityService.page(condition);
    }

}
