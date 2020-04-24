package com.quinn.framework.service.impl;

import com.quinn.framework.exception.DataOperationTransactionException;
import com.quinn.framework.model.CallableObject;
import com.quinn.framework.model.NextNumSeqValue;
import com.quinn.framework.service.JdbcService;
import com.quinn.framework.util.enums.CallableTypeEnum;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;
import com.quinn.util.base.util.CollectionUtil;
import com.quinn.util.base.util.StringUtil;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.SqlConstant;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.DataOperateTypeEnum;
import com.quinn.util.constant.enums.ExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.quinn.util.constant.enums.ExceptionEnum.DATA_OPERATION_TRANSACTION_TERMINATED;

/**
 * 数据库直接操作层
 *
 * @author Qunhua.Liao
 * @since 202-04-04
 */
public class JdbcServiceImpl implements JdbcService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BatchResult<String> executeUpdateBatch(String sql, boolean transaction) {
        if (StringUtil.isEmpty(sql)) {
            return BatchResult.build(sql, true, 0).getRecentItem()
                    .buildMessage(ExceptionEnum.PARAM_SHOULD_NOT_NULL.name(), 1, 0)
                    .addParam(ExceptionEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "sql")
                    .result();
        }

        String[] sqlArray = sql.split(";+");
        int length = sqlArray.length;
        BatchResult<String> result = new BatchResult<>(length);
        int count = 0;

        for (int i = 0; i < length; i++) {
            if (StringUtil.isEmpty(sqlArray[i].trim())) {
                continue;
            }

            try {
                int update = jdbcTemplate.update(sqlArray[i]);
                count += update;
                result.successData(sqlArray[i]).ofLevel(update);
            } catch (Exception e) {
                if (transaction) {
                    throw new DataOperationTransactionException()
                            .getMessageProp()
                            .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[0], DataOperateTypeEnum.OPERATE.name())
                            .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[1], StringConstant.STRING_EMPTY)
                            .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[2], sqlArray.length)
                            .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[3], i)
                            .exception();
                } else {
                    result.failData(sqlArray[i]).setMessage(e.getMessage());
                }
            }
        }

        result.setLevel(count);
        return result;
    }

    @Override
    public BaseResult<Integer> executeUpdate(String sql, Object... params) {
        return BaseResult.success(jdbcTemplate.update(sql, params));
    }

    @Override
    public <T> BaseResult<T> executeQueryForObject(String sql, Class<T> clazz, Object... params) {
        T t = jdbcTemplate.queryForObject(sql, params, clazz);
        if (t == null) {
            return BaseResult.fail()
                    .buildMessage(ExceptionEnum.RESULT_NOT_FOUND.name(), 0, 1)
                    .addParam(ExceptionEnum.RESULT_NOT_FOUND.paramNames[0], StringConstant.STRING_EMPTY)
                    .result()
                    ;
        }
        return BaseResult.success(t);
    }

    @Override
    public <T> BaseResult<List<T>> executeQueryForList(String sql, Class<T> clazz, Object... params) {
        List<T> list = jdbcTemplate.queryForList(sql, params, clazz);
        if (CollectionUtil.isEmpty(list)) {
            return BaseResult.fail()
                    .buildMessage(ExceptionEnum.RESULT_NOT_FOUND.name(), 0, 1)
                    .addParam(ExceptionEnum.RESULT_NOT_FOUND.paramNames[0], StringConstant.STRING_EMPTY)
                    .result()
                    ;
        }
        return BaseResult.success(list);
    }

    @Override
    public BaseResult<Long> generateNextValueOfSeq(String seqName) {
        CallableObject<Long> callableObject =
                CallableObject.build(CallableTypeEnum.FUNCTION, SqlConstant.SEQ_NEXT_VALUE, Long.class, 2)
                        .addOutParam(SqlConstant.PARAM_SEQ_VALUE, Types.BIGINT)
                        .addInParam(SqlConstant.PARAM_SEQ_NAME, seqName)
                ;
        return executeCallableForObject(callableObject);
    }

    @Override
    public BaseResult<NextNumSeqValue> generateNextNumValueOfSeq(String seqName, int num) {
        CallableObject<NextNumSeqValue> callableObject =
                CallableObject.build(CallableTypeEnum.FUNCTION, SqlConstant.SEQ_NEXT_VALUE, NextNumSeqValue.class, 2)
                        .addOutParam(SqlConstant.PARAM_SEQ_VALUE, Types.BIGINT)
                        .addInParam(SqlConstant.PARAM_SEQ_NAME, seqName)
                ;
        return executeCallableForObject(callableObject);
    }

    @Override
    public <T> BaseResult<List<T>> executeCallableForList(CallableObject<T> callableObject) {
        List<T> data = jdbcTemplate.execute(new CallableCallableStatementCreator(callableObject), cs -> {
            cs.execute();
            ResultSet result = cs.getResultSet();
            if (result != null) {
                List<T> list = new ArrayList<>();
                int rowNum = 1;
                Class<T> clazz = callableObject.getResultClass();
                if (BaseConverter.isPrimitive(clazz)) {
                    while (result.next()) {
                        list.add(BaseConverter.staticConvert(result.getObject(NumberConstant.INT_ONE), clazz));
                    }
                } else {
                    RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(clazz);
                    while (result.next()) {
                        list.add(rowMapper.mapRow(result, rowNum++));
                    }
                }
                return list;
            }
            return null;
        });

        return BaseResult.success(data);
    }

    @Override
    public <T> BaseResult<T> executeCallableForObject(CallableObject<T> callableObject) {
        T data = jdbcTemplate.execute(new CallableCallableStatementCreator(callableObject), cs -> {
            cs.execute();
            Class<T> clazz = callableObject.getResultClass();
            if (BaseConverter.isPrimitive(clazz)) {
                return BaseConverter.staticConvert(cs.getObject(NumberConstant.INT_ONE), clazz);
            } else {
                ResultSet result = cs.getResultSet();
                if (result != null) {
                    RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(clazz);
                    return rowMapper.mapRow(result, 1);
                }
            }
            return null;
        });

        return BaseResult.success(data);
    }


    /**
     * CallableStatement创建器
     *
     * @author Qunhua.Liao
     * @since 2020-04-05
     */
    public class CallableCallableStatementCreator implements CallableStatementCreator {

        CallableObject callableObject;

        public CallableCallableStatementCreator(CallableObject callableObject) {
            this.callableObject = callableObject;
        }

        @Override
        public CallableStatement createCallableStatement(Connection connection) throws SQLException {
            String sql = callableObject.toSql();
            CallableStatement cs = connection.prepareCall(sql);
            List<CallableObject.CallableParam> params = callableObject.getParams();
            if (!CollectionUtil.isEmpty(params)) {
                for (CallableObject.CallableParam param : params) {
                    if ((param.getDirect().code & 1) > 0) {
                        cs.setObject(param.getIndex(), param.getValue());
                    }

                    if ((param.getDirect().code & 2) > 0) {
                        cs.registerOutParameter(param.getIndex(), param.getValueType());
                    }
                }
            }
            return cs;
        }
    }

}
