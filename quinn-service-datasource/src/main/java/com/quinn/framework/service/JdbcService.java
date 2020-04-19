package com.quinn.framework.service;

import com.quinn.framework.model.CallableObject;
import com.quinn.framework.model.NextNumSeqValue;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;

import java.util.List;
import java.util.Map;

/**
 * JDBC直接操作接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-04
 */
public interface JdbcService {

    /**
     *直接执行DDL-SQL \ DML-SQL
     *
     * @param sql           SQL
     * @param transaction   是否事务管理
     * @return              执行结果
     */
    BatchResult<String> executeUpdate(String sql, boolean transaction);

    /**
     *直接执行SQL
     *
     * @param sql       查询SQL
     * @param clazz     返回实体类
     * @param params    参数
     * @return          执行结果
     */
    <T> BaseResult<T> executeQueryForObject(String sql, Class<T> clazz, Object... params);

    /**
     *直接执行SQL
     *
     * @param sql       查询SQL
     * @param clazz     返回实体类
     * @param params    参数
     * @return          执行结果
     */
    <T> BaseResult<List<T>> executeQueryForList(String sql, Class<T> clazz, Object... params);

    /**
     * 获取序列下一个值
     *
     * @param seqName 序列名
     * @return  序列
     */
    BaseResult<Long> generateNextValueOfSeq(String seqName);

    /**
     * 获取序列下N个值
     *
     * @param seqName 序列名
     * @param num 若干个值
     * @return 起始值、步长
     */
    BaseResult<NextNumSeqValue> generateNextNumValueOfSeq(String seqName, int num);

    /**
     * 执行存储过程
     *
     * @param callableObject 出参类型
     * @return 出参
     */
    <T> BaseResult<List<T>> executeCallableForList(CallableObject<T> callableObject);

    /**
     * 执行存储过程
     *
     * @param callableObject 出参类型
     * @return 出参
     */
    <T> BaseResult<T> executeCallableForObject(CallableObject<T> callableObject);

}
