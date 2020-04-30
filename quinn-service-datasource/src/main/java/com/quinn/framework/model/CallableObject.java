package com.quinn.framework.model;

import com.quinn.framework.util.enums.CallableParameterDirectionEnum;
import com.quinn.framework.util.enums.CallableTypeEnum;
import com.quinn.framework.util.enums.JdbcTypeEnum;
import com.quinn.util.base.util.CollectionUtil;
import com.quinn.util.constant.CharConstant;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储过程、函数对象
 *
 * @author Qunhua.Liao
 * @since 2020-04-05
 */
@Setter
@Getter
public class CallableObject {

    private CallableObject() {
    }

    /**
     * 存储过程、函数
     */
    private CallableTypeEnum callableType;

    /**
     * 存储过程名称
     */
    private String name;

    /**
     * 返回结果类型
     */
    private Class resultClass;

    /**
     * 存储过程参数
     */
    private List<CallableParam> params;

    /**
     * 基本参数构建
     *
     * @param callableType 类型
     * @param name         名称
     * @param resultClass  返回值类型
     * @param paramSize    参数个数
     * @return
     */
    public static  CallableObject build(CallableTypeEnum callableType, String name, Class resultClass, int paramSize) {
        CallableObject callableObject = new CallableObject();
        callableObject.setCallableType(callableType);
        callableObject.setName(name);
        callableObject.setResultClass(resultClass);
        callableObject.params = new ArrayList<>(paramSize);
        return callableObject;
    }

    /**
     * 添加入参
     *
     * @return 本身
     */
    public <P> CallableObject addInParam(String paramName, P paramValue) {
        CallableParam callableParam = addParam(paramName);
        if (paramValue != null) {
            callableParam.setValueType(JdbcTypeEnum.getJdbcSqlTypeByName(paramValue.getClass()).code);
        }

        callableParam.setDirect(CallableParameterDirectionEnum.IN);
        callableParam.setValue(paramValue);
        return this;
    }

    /**
     * 添加出参
     *
     * @return 本身
     */
    public CallableObject addOutParam(String paramName, int jdbcType) {
        CallableParam callableParam = addParam(paramName);
        callableParam.setDirect(CallableParameterDirectionEnum.OUT);
        callableParam.setValueType(jdbcType);
        return this;
    }

    /**
     * 添加全参
     *
     * @return 本身
     */
    public <P> CallableObject addBothParam(String paramName, P paramValue, int jdbcType) {
        CallableParam callableParam = addParam(paramName);
        callableParam.setValue(paramValue);
        callableParam.setDirect(CallableParameterDirectionEnum.BOTH);
        callableParam.setValueType(jdbcType);
        return this;
    }

    /**
     * 添加参数通用
     *
     * @param paramName 参数名
     * @return 参数对象
     */
    private CallableParam addParam(String paramName) {
        CallableParam callableParam = new CallableParam();
        callableParam.setIndex(params.size() + 1);
        callableParam.setName(paramName);
        params.add(callableParam);
        return callableParam;
    }

    /**
     * 变为SQL文
     * {call t_customer_select (?,?,?)}
     * {? = call t_customer_select (?,?)}
     *
     * @return SQL文
     */
    public String toSql() {
        StringBuilder query = new StringBuilder();
        query.append(CharConstant.OPEN_BRACE);
        if (callableType == CallableTypeEnum.FUNCTION) {
            query.append("? = ");
        }

        query.append("call ").append(name).append(CharConstant.OPEN_PAREN);

        if (!CollectionUtil.isEmpty(params)) {
            for (CallableParam ignored : params) {
                if (callableType == CallableTypeEnum.FUNCTION
                        && ignored.getDirect() == CallableParameterDirectionEnum.OUT) {
                    continue;
                }
                query.append(CharConstant.QUESTION_MARK).append(CharConstant.COMMA);
            }
            query.deleteCharAt(query.length() - 1);
        }

        query.append(CharConstant.CLOSE_PAREN)
                .append(CharConstant.CLOSE_BRACE)
        ;

        return query.toString();
    }

    /**
     * 存储过程、函数参数
     *
     * @author Qunhua.Liao
     * @since 2020-04-05
     */
    @Setter
    @Getter
    public class CallableParam<P> {

        /**
         * 方向
         */
        private CallableParameterDirectionEnum direct;

        /**
         * 下表索引
         */
        private int index;

        /**
         * 属性
         */
        private String name;

        /**
         * 类型
         */
        private int valueType;

        /**
         * 参数值
         */
        private P value;

        public boolean isOut() {
            return CallableParameterDirectionEnum.OUT == direct || CallableParameterDirectionEnum.BOTH == direct;
        }

        /**
         * 返回持有当前参数对象的CallableObject对象
         *
         * @return CallableObject对象
         */
        public CallableObject object() {
            return CallableObject.this;
        }

    }

}
