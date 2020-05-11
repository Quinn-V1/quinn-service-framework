package com.quinn.framework.controller;

import com.quinn.framework.component.SpringBeanHolder;
import com.quinn.framework.model.NextNumSeqValue;
import com.quinn.framework.service.BaseEntityService;
import com.quinn.framework.service.JdbcService;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据管理员操作
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
@RestController
@RequestMapping("/framework/data-admin/*")
@Api(tags = {"0ZY070管理：数据超级权限"})
public class DataAdminController extends AbstractController {

    @Resource
    private JdbcService jdbcService;

    /**
     * 恢复数据
     *
     * @param dataClass 数据类型
     * @param dataId    数据主键
     * @return 恢复结果
     */
    @ApiOperation(value = "恢复数据", httpMethod = "POST")
    @PostMapping(value = "recovery-data")
    public BaseResult recoveryData(
            @ApiParam(name = "dataClass", value = "数据类型", required = true)
            @RequestParam(name = "dataClass") String dataClass,

            @ApiParam(name = "dataId", value = "数据主键", required = true)
            @RequestParam(name = "dataId") Long dataId
    ) {
        try {
            Class clazz = Class.forName(dataClass);
            BaseEntityService baseEntityService = SpringBeanHolder.getServiceByEntityClass(clazz);
            if (baseEntityService == null) {
                return BaseResult.fail("数据类型不对，请联系管理员进行操作");
            }
            return baseEntityService.recovery(dataId);
        } catch (ClassNotFoundException e) {
            return BaseResult.fail("数据类型不对，请联系管理员进行操作");
        }
    }

    /**
     * 直接执行DDL-SQL \ DML-SQL
     *
     * @param sql       SQL
     * @param dataClass 数据类型
     * @return 执行结果
     */
    @ApiOperation(value = "直接执行DQL-SQL", httpMethod = "POST")
    @PostMapping(value = "execute-query")
    public BaseResult executeQuery(
            @ApiParam(name = "sql", value = "执行SQL", required = true)
            @RequestParam(name = "sql") String sql,

            @ApiParam(name = "dataClass", value = "数据类型", required = true)
            @RequestParam(name = "dataClass") String dataClass
    ) {
        try {
            Class clazz = Class.forName(dataClass);
            return jdbcService.queryForList(sql, clazz);
        } catch (ClassNotFoundException e) {
            return BaseResult.fail("数据类型不对，请联系管理员进行操作");
        }
    }

    /**
     * 直接执行DDL-SQL \ DML-SQL
     *
     * @param sql         SQL
     * @param transaction 是否事务管理
     * @return 执行结果
     */
    @ApiOperation(value = "直接执行DDL-SQL、DML-SQL", httpMethod = "POST")
    @PostMapping(value = "execute-update")
    public BatchResult<String> executeUpdate(
            @ApiParam(name = "sql", value = "执行SQL", required = true)
            @RequestParam(name = "sql") String sql,

            @ApiParam(name = "transaction", value = "事务控制", required = true)
            @RequestParam(name = "transaction") boolean transaction
    ) {
        return jdbcService.updateBatch(sql, transaction);
    }

    /**
     * 获取序列下一个值
     *
     * @param seqName 序列名
     * @return 序列
     */
    @ApiOperation(value = "恢复数据", httpMethod = "POST")
    @PostMapping(value = "next-value")
    public BaseResult<Long> generateNextValueOfSeq(
            @ApiParam(name = "seqName", value = "序列名称", required = true)
            @RequestParam(name = "seqName") String seqName
    ) {
        return jdbcService.generateNextValueOfSeq(seqName);
    }

    /**
     * 获取序列下N个值
     *
     * @param seqName 序列名
     * @param seqNum  若干个值
     * @return 起始值、步长
     */
    @ApiOperation(value = "获取序列下N个值", httpMethod = "POST")
    @PostMapping(value = "next-num-value")
    public BaseResult<NextNumSeqValue> generateNextNumValueOfSeq(
            @ApiParam(name = "seqName", value = "序列名称", required = true)
            @RequestParam(name = "seqName") String seqName,

            @ApiParam(name = "seqNum", value = "序列个数", required = true)
            @RequestParam(name = "seqNum") int seqNum
    ) {
        return jdbcService.generateNextNumValueOfSeq(seqName, seqNum);
    }


}
