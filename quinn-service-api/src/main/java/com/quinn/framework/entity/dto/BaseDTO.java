package com.quinn.framework.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quinn.framework.util.SessionUtil;
import com.quinn.framework.util.enums.SqlCondWrapperEnum;
import com.quinn.framework.util.enums.SqlPropWrapperEnum;
import com.quinn.framework.util.enums.UpdateTypeEnum;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.constant.CharConstant;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.AvailableStatusEnum;
import com.quinn.util.constant.enums.DataStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础数据传输类
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
@Setter
@Getter
public abstract class BaseDTO<T> {

    /**
     * 缓存主键分割符
     */
    public static final String CACHE_KEY_DELIMITER = StringConstant.CHAR_COLON;

    /**
     * 数据主键分割符
     */
    public static final String DATA_KEY_DELIMITER = StringConstant.CHAR_VERTICAL_BAR;

    /**
     * 属性主键分割符
     */
    public static final String PROPERTY_DELIMITER = StringConstant.CHAR_POUND_SIGN;

    {
        // 默认不做查询条数限制
        limit = -1;

        // 默认查找有效数据
        dataVersionFrom = 1;

        // 数据状态默认启用
        dataStatusFrom = DataStatusEnum.NORMAL.code;

        // 使用缓存
        useCache = true;
    }

    /**
     * 是否使用缓存
     */
    private Boolean useCache;

    /**
     * 期望结果数：如果确定只找一个结果，加一个limit 2
     */
    @ApiModelProperty("期望结果数")
    private Integer resultNumExpected;

    /**
     * 系统主键
     */
    @ApiModelProperty("系统主键")
    private Long id;

    /**
     * 实体类对象
     */
    @JsonIgnore
    private Class<T> entityClass;

    /**
     * 限制条数
     */
    @JsonIgnore
    private Integer limit;

    /**
     * 关键字
     */
    @ApiModelProperty("关键字")
    private String keyWords;

    /**
     * 排序规则
     */
    @ApiModelProperty("排序规则")
    private String orderBy;

    /**
     * 操作者(用作权限控制)
     */
    @ApiModelProperty("操作者")
    private String operator;

    /**
     * 操作者组织（用作权限控制）
     */
    @ApiModelProperty("操作者组织")
    private String operatorOrg;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private LocalDateTime operateTime;

    /**
     * 顶层组织
     */
    @ApiModelProperty("顶层组织")
    private String rootOrg;

    /**
     * 数据版本号开始
     */
    @ApiModelProperty("数据版本号开始")
    private Integer dataVersionFrom;

    /**
     * 数据版本号结束
     */
    @ApiModelProperty("数据版本号结束")
    private Integer dataVersionTo;

    /**
     * 创建时间开始
     */
    @ApiModelProperty("数据版本号开始")
    private LocalDateTime insertDateTimeFrom;

    /**
     * 创建时间结束
     */
    @ApiModelProperty("创建时间结束")
    private LocalDateTime insertDateTimeTo;

    /**
     * 更新时间开始
     */
    @ApiModelProperty("更新时间开始")
    private LocalDateTime updateDateTimeFrom;

    /**
     * 更新时间结束
     */
    @ApiModelProperty("更新时间结束")
    private LocalDateTime updateDateTimeTo;

    /**
     * 数据状态
     */
    @ApiModelProperty("数据状态")
    private Integer dataStatus;

    /**
     * 数据状态开始
     */
    @ApiModelProperty("数据状态开始")
    private Integer dataStatusFrom;

    /**
     * 数据状态结束
     */
    @ApiModelProperty("数据状态结束")
    private Integer dataStatusTo;

    /**
     * 创建用户
     */
    @ApiModelProperty("创建用户")
    private String insertUser;

    /**
     * 更新用户
     */
    @ApiModelProperty("更新用户")
    private String updateUser;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private List<OrderField> orderFields;

    /**
     * 设置生效状态
     */
    @ApiModelProperty("生效状态")
    public void setAvailableStatus(AvailableStatusEnum availableStatus) {
        if (availableStatus == null) {
            return;
        }

        switch (availableStatus) {
            case ALL:
                dataStatusFrom = dataVersionTo = null;
                break;
            case UNAVAILABLE:
                dataStatusFrom = null;
                dataStatusTo = NumberConstant.INT_ONE_NEGATIVE;
                break;
            case AVAILABLE:
            default:
                dataStatusFrom = NumberConstant.INT_ONE;
                dataStatusTo = null;
                break;
        }
    }

    /**
     * 获取或者生成排序字符串
     *
     * @return 排序字符串
     */
    public String getOrderBy() {
        if (orderBy != null) {
            return orderBy;
        }

        if (CollectionUtil.isEmpty(orderFields)) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        for (OrderField orderField : orderFields) {
            String column = columnOfProp(orderField.getProp());
            if (StringUtil.isEmpty(column)) {
                continue;
            }

            query.append(orderField.ofAlias()).append(CharConstant.DOT)
                    .append(column).append(CharConstant.BLANK)
                    .append(orderField.ofOrder()).append(", ")
            ;
        }

        if (query.length() > 0) {
            query.delete(query.length() - 2, query.length());
        }

        orderBy = query.toString();
        return orderBy;
    }

    /**
     * 添加排序字段
     *
     * @param prop  属性名
     * @param order 排序
     */
    public void addOrderBy(String prop, String order) {
        if (orderFields == null) {
            orderFields = new ArrayList<>();
        }
        orderFields.add(new OrderField(prop, order, null));
    }

    /**
     * 添加排序字段
     *
     * @param prop  属性名
     * @param alias 别名
     * @param order 排序
     */
    public void addOrderBy(String prop, String order, String alias) {
        if (orderFields == null) {
            orderFields = new ArrayList<>();
        }
        orderFields.add(new OrderField(prop, order, alias));
    }

    /**
     * 关键字SQL条件字符串（模糊匹配）
     *
     * @return 关键字SQL条件字符串（模糊匹配）
     */
    private String getKeyWordsLikeCond() {
        return StringUtil.isEmpty(keyWords) ? null : "%" + keyWords + "%";
    }

    /**
     * 关键字SQL条件字符串（模糊匹配）
     *
     * @return 关键字SQL条件字符串（模糊匹配）
     */
    private String getKeyWordsLikeStartCond() {
        return StringUtil.isEmpty(keyWords) ? null : keyWords + "%";
    }

    /**
     * 设置实体类对象
     *
     * @param <V>         结果泛型
     * @param entityClass 实体类对象
     * @return 本身
     */
    public <V extends BaseDTO> V ofEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
        return (V) this;
    }

    /**
     * 获取数据编码
     *
     * @return
     */
    public String dataKey() {
        return String.valueOf(id);
    }

    /**
     * 逆向解析数据编码
     */
    public boolean dataKey(String dataKey) {
        return dataKey(dataKey, DATA_KEY_DELIMITER);
    }

    /**
     * 逆向解析数据编码
     */
    public boolean dataKey(String dataKey, String delimiter) {
        this.id = BaseConverter.staticConvert(dataKey, Long.class);
        return true;
    }

    /**
     * 获取缓存键
     */
    public String cacheKey() {
        if (entityClass == null) {
            return null;
        }
        return entityClass.getSimpleName() + CACHE_KEY_DELIMITER + dataKey();
    }

    /**
     * 逆向解析缓存键
     */
    public void cacheKey(String cacheKey) {
        cacheKey(cacheKey, DATA_KEY_DELIMITER);
    }

    /**
     * 逆向解析缓存键
     */
    public void cacheKey(String cacheKey, String delimiter) {
        if (entityClass != null) {
            cacheKey = cacheKey.substring(entityClass.getSimpleName().length() + delimiter.length());
        }
        dataKey(cacheKey, delimiter);
    }

    @Override
    public String toString() {
        return this.cacheKey();
    }

    /**
     * 根据属性找列名
     *
     * @param prop 属性
     * @return 列名
     */
    public abstract String columnOfProp(String prop);

    /**
     * 获取表名
     *
     * @return 表名
     */
    public abstract String tableName();

    /**
     * 自有查询对象
     *
     * @param clazz      结果对象
     * @param condSize   条件列数
     * @param resultSize 结果列数
     * @return 自有查询对象
     */
    public FreeQuery freeQuery(Class clazz, int condSize, int resultSize) {
        FreeQuery freeQuery = new FreeQuery(clazz, condSize, resultSize);
        return freeQuery;
    }

    /**
     * 自有查询对象
     *
     * @param clazz       结果对象
     * @param condSize    条件列数
     * @param resultProps 结果列数
     * @return 自有查询对象
     */
    public FreeQuery freeQuery(Class clazz, int condSize, String... resultProps) {
        FreeQuery freeQuery = new FreeQuery(clazz, condSize, resultProps);
        return freeQuery;
    }

    /**
     * 自有新增
     *
     * @param valueSize 值大小
     * @param condSize  条件大小
     * @return 自由更新对象
     */
    public FreeUpdate freeUpdate(int valueSize, int condSize) {
        return new FreeUpdate(UpdateTypeEnum.UPDATE).valueSize(valueSize).condSize(condSize);
    }

    /**
     * 自有新增
     *
     * @param condSize 条件大小
     * @return 自由删除对象
     */
    public FreeUpdate freeIDelete(int condSize) {
        return new FreeUpdate(UpdateTypeEnum.DELETE).condSize(condSize);
    }

    /**
     * 自有新增
     *
     * @param valueSize 值大小
     * @return 自由新增对象
     */
    public FreeUpdate freeInsert(int valueSize) {
        return new FreeUpdate(UpdateTypeEnum.INSERT).valueSize(valueSize);
    }

    /**
     * 权限校验条件
     *
     * @param force 强制
     */
    public void initDataAuth(boolean force) {
        if (force) {
            setOperator(SessionUtil.getUserKey());
            setOperatorOrg(SessionUtil.getOrgKey());
        } else {
            if (StringUtil.isEmptyInFrame(operator)) {
                setOperator(SessionUtil.getUserKey());
            }
            if (StringUtil.isEmptyInFrame(operatorOrg)) {
                setOperatorOrg(SessionUtil.getOrgKey());
            }
        }
    }

    /**
     * 自有查询对象
     *
     * @author Qunhua.Liao
     * @since 2020-04-24
     */
    public class FreeQuery<V> {

        /**
         * 条件字段
         */
        private List<FieldValue> condFields;

        /**
         * 结果字段
         */
        private List<ResultField> resultFields;

        /**
         * 结果类型
         */
        private Class<V> resultClass;

        /**
         * 参数
         */
        private Object[] params;

        /**
         * 分组字段
         */
        private String[] groupFields;

        /**
         * 获取结果类型
         *
         * @return 结果类型
         */
        public Class<V> getResultClass() {
            return resultClass;
        }

        /**
         * 获取参数
         *
         * @return 参数
         */
        public Object[] getParams() {
            return params;
        }

        /**
         * 带参构造器
         *
         * @param resultClass 结果类型
         * @param condSize    条件数
         * @param resultSize  结果数
         */
        private FreeQuery(Class<V> resultClass, int condSize, int resultSize) {
            this.resultClass = resultClass;
            this.condFields = new ArrayList<>(condSize);
            this.resultFields = new ArrayList<>(resultSize);
        }

        /**
         * 带参构造器
         *
         * @param resultClass 结果类型
         * @param condSize    条件数
         * @param props       结果数
         */
        private FreeQuery(Class<V> resultClass, int condSize, String... props) {
            this.resultClass = resultClass;
            this.condFields = new ArrayList<>(condSize);

            if (props != null) {
                this.resultFields = new ArrayList<>(props.length);
                for (String prop : props) {
                    wrapResultField(prop, null);
                }
            }
        }

        /**
         * 生成SQL文件
         *
         * @return SQL文
         */
        public String generateSql() {
            StringBuilder query = new StringBuilder();
            query.append("SELECT ");
            for (ResultField resultField : resultFields) {
                query.append(resultField.fullName()).append(",");
            }

            query.deleteCharAt(query.length() - 1);
            query.append(" FROM ").append(tableName());

            if (!CollectionUtil.isEmpty(condFields)) {
                query.append(" WHERE ");
                params = new Object[condFields.size()];
                for (int i = 0; i < condFields.size(); i++) {
                    FieldValue fieldValue = condFields.get(i);
                    query.append(fieldValue.fullValue())
                            .append(" AND ")
                    ;
                    params[i] = fieldValue.value;
                }

                query.delete(query.length() - 5, query.length());
            }

            if (!CollectionUtil.isEmpty(groupFields)) {
                query.append(" GROUP BY ");
                for (String groupField : groupFields) {
                    query.append(columnOfProp(groupField)).append(", ");
                }
                query.delete(query.length() - 2, query.length());
            }

            String orderBy = getOrderBy();
            if (!StringUtil.isEmpty(orderBy)) {
                query.append(" ORDER BY").append(orderBy);
            }

            return query.toString();
        }

        /**
         * 包裹 属性
         *
         * @param prop    属性名
         * @param wrapper 包装方式
         * @return 本身
         */
        public FreeQuery<V> wrapResultField(String prop, SqlPropWrapperEnum wrapper) {
            resultFields.add(new ResultField(prop, wrapper));
            return this;
        }

        /**
         * 增加参数
         *
         * @param prop  属性
         * @param value 参数
         * @return 本身
         */
        public FreeQuery<V> addParamField(String prop, Object value) {
            if (value != null && !StringConstant.STRING_EMPTY.equals(value)) {
                condFields.add(new FieldValue(prop, value));
            }
            return this;
        }

        /**
         * 增加参数
         *
         * @param prop  属性
         * @param value 参数
         * @return 本身
         */
        public FreeQuery<V> addParamField(String prop, Object value, SqlCondWrapperEnum wrapper) {
            if (value != null && !StringConstant.STRING_EMPTY.equals(value)) {
                condFields.add(new FieldValue(prop, value, wrapper));
            }
            return this;
        }

        /**
         * 添加有效限制参数
         *
         * @return 本身
         */
        public FreeQuery<V> addAvailableParam() {
            if (condFields == null) {
                condFields = new ArrayList<>(2);
            }

            condFields.add(new FieldValue("dataVersion", NumberConstant.INT_ONE, SqlCondWrapperEnum.GREAT_EQUAL));
            condFields.add(new FieldValue("dataStatus", DataStatusEnum.NORMAL.code,
                    SqlCondWrapperEnum.GREAT_EQUAL));
            return this;
        }

        /**
         * 添加分组字段
         *
         * @param props 分组属性名
         * @return 本身
         */
        public FreeQuery<V> groupBy(String... props) {
            this.groupFields = props;
            return this;
        }

    }

    /**
     * 自由更新对象
     *
     * @author Qunhua.Liao
     * @since 2020-04-24
     */
    public class FreeUpdate {

        private FreeUpdate(UpdateTypeEnum updateType) {
            this.updateType = updateType;
        }

        /**
         * 设置值列
         *
         * @param size 列数
         * @return 本身
         */
        private FreeUpdate valueSize(int size) {
            valueFields = new ArrayList<>(size);
            return this;
        }

        /**
         * 设置值列
         *
         * @param size 列数
         * @return 本身
         */
        private FreeUpdate condSize(int size) {
            condFields = new ArrayList<>(size);
            return this;
        }

        /**
         * 更新类型
         */
        private UpdateTypeEnum updateType;

        /**
         * 值字段
         */
        private List<FieldValue> valueFields;

        /**
         * 条件字段
         */
        private List<FieldValue> condFields;

        /**
         * 参数
         */
        private Object[] params;

        /**
         * 生成SQL
         *
         * @return SQL
         */
        public String generateSql() {
            StringBuilder query = new StringBuilder();

            switch (updateType) {
                case UPDATE:
                    query.append("UPDATE ").append(tableName()).append(" SET ");
                    params = new Object[valueFields.size() + condFields.size()];
                    int k = 0;
                    for (; k < valueFields.size(); k++) {
                        FieldValue fieldValue = valueFields.get(k);
                        query.append(columnOfProp(fieldValue.prop)).append(" = ?,");
                        params[k] = fieldValue.value;
                    }
                    query.deleteCharAt(query.length() - 1);

                    query.append(" WHERE ");
                    for (int i = 0; i < condFields.size(); i++) {
                        FieldValue fieldValue = condFields.get(i);
                        query.append(columnOfProp(fieldValue.prop)).append(" = ? AND ");
                        params[k + i] = fieldValue.value;
                    }
                    query.delete(query.length() - 5, query.length());

                    break;
                case INSERT:
                    query.append("INSERT INTO ").append(tableName()).append("(");
                    StringBuilder queryVal = new StringBuilder();

                    params = new Object[valueFields.size()];

                    for (int i = 0; i < valueFields.size(); i++) {
                        FieldValue FieldValue = valueFields.get(i);
                        query.append(columnOfProp(FieldValue.prop)).append(",");
                        queryVal.append("?,");
                        params[i] = FieldValue.value;
                    }
                    query.deleteCharAt(query.length() - 1);
                    queryVal.deleteCharAt(queryVal.length() - 1);

                    query.append(") VALUES (");
                    query.append(queryVal).append(")");

                case DELETE:
                    query.append("DELETE FROM ").append(tableName()).append(" WHERE ");
                    params = new Object[condFields.size()];

                    for (int i = 0; i < condFields.size(); i++) {
                        FieldValue FieldValue = condFields.get(i);
                        query.append(columnOfProp(FieldValue.prop)).append(" = ? AND ");
                        params[i] = FieldValue.value;
                    }
                    query.delete(query.length() - 5, query.length());
                    break;
                default:
                    break;
            }

            return query.toString();
        }

        /**
         * 获取参数
         *
         * @return 参数
         */
        public Object[] getParams() {
            return params;
        }

        /**
         * 增加参数
         *
         * @param prop  属性
         * @param value 参数
         */
        public FreeUpdate addValueField(String prop, Object value) {
            valueFields.add(new FieldValue(prop, value));
            return this;
        }

        /**
         * 增加参数
         *
         * @param prop  属性
         * @param value 参数
         */
        public FreeUpdate addParamField(String prop, Object value) {
            condFields.add(new FieldValue(prop, value));
            return this;
        }
    }

    /**
     * 条件字段
     *
     * @author Qunhua.Liao
     * @since 2020-04-24
     */
    private class FieldValue {

        private FieldValue(String prop, Object value) {
            this.prop = prop;
            this.value = value;
        }

        private FieldValue(String prop, Object value, SqlCondWrapperEnum wrapper) {
            this.prop = prop;
            this.value = value;
            this.wrapper = wrapper;
        }

        /**
         * 属性名称
         */
        String prop;

        /**
         * 条件值
         */
        Object value;

        /**
         * 包装方式
         */
        SqlCondWrapperEnum wrapper;

        /**
         * 全条件值
         *
         * @return 全条件值
         */
        public String fullValue() {
            if (wrapper == null) {
                return columnOfProp(prop) + StringConstant.CHAR_EQUAL_MARK + StringConstant.CHAR_QUESTION_MARK;
            } else {
                return columnOfProp(prop) + wrapper.wrap(StringConstant.CHAR_QUESTION_MARK);
            }
        }

    }

    /**
     * 结果字段
     *
     * @author Qunhua.Liao
     * @since 2020-04-24
     */
    private class ResultField {

        private ResultField(String prop, SqlPropWrapperEnum wrapper) {
            this.prop = prop;
            this.wrapper = wrapper;
        }

        /**
         * 属性名称
         */
        String prop;

        /**
         * 包装方式
         */
        SqlPropWrapperEnum wrapper;

        /**
         * 全字段名
         *
         * @return 全字段名
         */
        public String fullName() {
            if (wrapper == null) {
                return columnOfProp(prop) + " AS " + prop;
            } else {
                return wrapper.wrap(columnOfProp(prop)) + " AS " + prop;
            }
        }
    }
}
