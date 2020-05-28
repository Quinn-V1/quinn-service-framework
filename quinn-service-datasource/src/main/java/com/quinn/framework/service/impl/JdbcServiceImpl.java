package com.quinn.framework.service.impl;

import com.quinn.framework.api.strategy.Strategy;
import com.quinn.framework.api.strategy.StrategyBean;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.exception.DataOperationTransactionException;
import com.quinn.framework.model.CallableObject;
import com.quinn.framework.model.NextNumSeqValue;
import com.quinn.framework.service.JdbcService;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.SqlConstant;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.DataOperateTypeEnum;
import com.quinn.util.base.enums.CommonMessageEnum;
import javax.annotation.Resource;

import com.quinn.util.database.enums.CallableTypeEnum;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.quinn.util.base.enums.CommonMessageEnum.DATA_OPERATION_TRANSACTION_TERMINATED;

/**
 * 数据库直接操作层
 *
 * @author Qunhua.Liao
 * @since 202-04-04
 */
@Service("jdbcService")
public class JdbcServiceImpl implements JdbcService, StrategyBean {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(JdbcServiceImpl.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public <T> BaseResult<T> getFree(BaseDTO.FreeQuery freeQuery) {
        return queryForObject(freeQuery.generateSql(), freeQuery.getResultClass(),
                freeQuery.getParams());
    }

    @Override
    public <T> BaseResult<List<T>> selectFree(BaseDTO.FreeQuery freeQuery) {
        return queryForObject(freeQuery.generateSql(), freeQuery.getResultClass(),
                freeQuery.getParams());
    }

    @Override
    public BaseResult<Integer> updateFree(BaseDTO.FreeUpdate freeUpdate) {
        return executeUpdate(freeUpdate.generateSql(), freeUpdate.getParams());
    }

    @Override
    public BatchResult<String> updateBatch(String sql, boolean transaction) {
        LOGGER.info("updateBatch: {}", sql);

        if (StringUtil.isEmpty(sql)) {
            return BatchResult.build(sql, true, 0).getRecentItem()
                    .buildMessage(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.name(), 1, 0)
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "sql")
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
        LOGGER.info("updateBatch: {} with params {}", sql, CollectionUtil.join(params));
        return BaseResult.success(jdbcTemplate.update(sql, params));
    }

    @Override
    public <T> BaseResult<T> queryForObject(String sql, Class<T> clazz, Object... params) {
        T t = jdbcTemplate.queryForObject(sql, params, clazz);
        if (t == null) {
            return BaseResult.fail()
                    .buildMessage(CommonMessageEnum.RESULT_NOT_FOUND.name(), 0, 1)
                    .addParam(CommonMessageEnum.RESULT_NOT_FOUND.paramNames[0], StringConstant.STRING_EMPTY)
                    .result()
                    ;
        }
        return BaseResult.success(t);
    }

    @Override
    public <T> BaseResult<List<T>> queryForList(String sql, Class<T> clazz, Object... params) {
        List<T> list = jdbcTemplate.queryForList(sql, params, clazz);
        if (CollectionUtil.isEmpty(list)) {
            return BaseResult.fail()
                    .buildMessage(CommonMessageEnum.RESULT_NOT_FOUND.name(), 0, 1)
                    .addParam(CommonMessageEnum.RESULT_NOT_FOUND.paramNames[0], StringConstant.STRING_EMPTY)
                    .result()
                    ;
        }
        return BaseResult.success(list);
    }

    @Override
    @Strategy("jdbcService.generateNextValueOfSeq")
    public BaseResult<Long> generateNextValueOfSeq(String seqName) {
        CallableObject callableObject =
                CallableObject.build(CallableTypeEnum.FUNCTION, SqlConstant.SEQ_NEXT_VALUE, Long.class, 2)
                        .addOutParam(SqlConstant.PARAM_SEQ_VALUE, Types.BIGINT)
                        .addInParam(SqlConstant.PARAM_SEQ_NAME, seqName)
                ;
        return executeCallableForObject(callableObject);
    }

    @Override
    @Strategy("jdbcService.generateNextNumValueOfSeq")
    public BaseResult<NextNumSeqValue> generateNextNumValueOfSeq(String seqName, int seqNum) {
        CallableObject callableObject =
                CallableObject.build(CallableTypeEnum.PROCEDURE, SqlConstant.SEQ_NEXT_N_VALUE, NextNumSeqValue.class, 4)
                        .addInParam(SqlConstant.PARAM_SEQ_NAME, seqName)
                        .addInParam(SqlConstant.SEQ_NUMBER, seqNum)
                        .addOutParam(SqlConstant.PARAM_SEQ_VALUE, Types.BIGINT)
                        .addOutParam(SqlConstant.PARAM_SEQ_STEP, Types.INTEGER)
                ;
        return executeCallableForObject(callableObject);
    }

    @Override
    public <T> BaseResult<List<T>> executeCallableForList(CallableObject callableObject) {
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
    public <T> BaseResult<T> executeCallableForObject(CallableObject callableObject) {
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
                } else {
                    List<CallableObject.CallableParam> params = callableObject.getParams();
                    try {
                        T t = clazz.newInstance();
                        int i = 1;
                        for (CallableObject.CallableParam param : params) {
                            try {
                                Field field = clazz.getDeclaredField(param.getName());
                                field.setAccessible(true);
                                if (param.isOut()) {
                                    field.set(t, cs.getObject(i));
                                } else {
                                    field.set(t, param.getValue());
                                }
                            } catch (Exception e) {
                            } finally {
                                i++;
                            }
                        }
                        return t;
                    } catch (Exception e) {
                    }
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
