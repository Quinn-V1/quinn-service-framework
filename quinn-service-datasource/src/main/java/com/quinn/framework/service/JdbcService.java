package com.quinn.framework.service;

import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.model.CallableObject;
import com.quinn.framework.model.NextNumSeqValue;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;

import java.util.List;

/**
 * JDBC直接操作接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-04
 */
public interface JdbcService extends FreeQueryService {

    /**
     * 自由查找
     *
     * @param freeQuery 自由查询对象
     * @param <T>       结果实体类
     * @return 查询结果
     */
    <T> BaseResult<T> getFree(BaseDTO.FreeQuery freeQuery);

    /**
     * 自由查找
     *
     * @param freeQuery 自由查询对象
     * @param <T>       结果实体类
     * @return 查询结果
     */
    <T> BaseResult<List<T>> selectFree(BaseDTO.FreeQuery freeQuery);

    /**
     * 自由更新
     *
     * @param freeUpdate 自由更新对象
     * @return 查询结果
     */
    BaseResult<Integer> updateFree(BaseDTO.FreeUpdate freeUpdate);

    /**
     * 直接执行SQL
     *
     * @param sql    查询SQL
     * @param clazz  返回实体类
     * @param params 参数
     * @return 执行结果
     */
    <T> BaseResult<T> queryForObject(String sql, Class<T> clazz, Object... params);

    /**
     * 直接执行SQL
     *
     * @param sql    查询SQL
     * @param clazz  返回实体类
     * @param params 参数
     * @return 执行结果
     */
    <T> BaseResult<List<T>> queryForList(String sql, Class<T> clazz, Object... params);

    /**
     * 直接执行DDL-SQL \ DML-SQL
     *
     * @param sql    SQL
     * @param params 参数
     * @return 执行结果
     */
    BaseResult<Integer> executeUpdate(String sql, Object... params);

    /**
     * 直接执行DDL-SQL \ DML-SQL
     *
     * @param sql         SQL
     * @param transaction 是否事务管理
     * @return 执行结果
     */
    BatchResult<String> updateBatch(String sql, boolean transaction);

    /**
     * 获取序列下一个值
     *
     * @param seqName 序列名
     * @return 序列
     */
    BaseResult<Long> generateNextValueOfSeq(String seqName);

    /**
     * 获取序列下N个值
     *
     * @param seqName 序列名
     * @param seqNum  若干个值
     * @return 起始值、步长
     */
    BaseResult<NextNumSeqValue> generateNextNumValueOfSeq(String seqName, int seqNum);

    /**
     * 执行存储过程
     *
     * @param callableObject 出参类型
     * @return 出参
     */
    <T> BaseResult<List<T>> executeCallableForList(CallableObject callableObject);

    /**
     * 执行存储过程
     *
     * @param callableObject 出参类型
     * @return 出参
     */
    <T> BaseResult<T> executeCallableForObject(CallableObject callableObject);

}
