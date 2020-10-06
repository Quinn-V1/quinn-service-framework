package com.quinn.framework.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quinn.framework.util.SessionUtil;
import com.quinn.framework.util.enums.*;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.exception.ParameterShouldNotEmpty;
import com.quinn.util.constant.CharConstant;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;
import com.quinn.util.constant.enums.AvailableStatusEnum;
import com.quinn.util.constant.enums.DataStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.*;

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
     * 默认表别名
     */
    public static final String DEFAULT_TABLE_ALIAS = "t";

    public static final String PROP_NAME_OF_ID = "id";

    public static final String PROP_NAME_OF_DATA_STATUS = "dataStatus";

    public static final String PROP_NAME_OF_DATA_VERSION = "dataVersion";

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
     * 是否分页处理
     */
    private Boolean pageFlag;

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
    @ApiModelProperty("操作者:用作用户权限控制")
    private String operator;

    /**
     * 操作者(用作角色权限控制)
     */
    @ApiModelProperty("操作者:用作角色权限控制")
    private String operatorForRole;

    /**
     * 操作者组织（用作权限控制）
     */
    @ApiModelProperty("操作者组织:用作组织权限控制")
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
                dataStatusFrom = null;
                dataVersionFrom = dataVersionTo = null;
                break;
            case UNAVAILABLE:
                dataStatusFrom = null;
                dataVersionFrom = null;
                dataVersionTo = NumberConstant.INT_ZERO;
                break;
            case AVAILABLE:
            default:
                dataVersionFrom = NumberConstant.INT_ONE;
                dataVersionTo = null;
                break;
        }
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
        return BaseConverter.staticToString(id);
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
        if (StringUtil.isEmpty(dataKey)) {
            return false;
        }
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
         * 结果字段
         */
        private List<ResultField> resultFields;

        /**
         * 条件字段
         */
        private List<FieldValue> condFields;

        /**
         * 分组字段
         */
        private List<GroupField> groupFields;

        /**
         * 关联查询表
         */
        private LinkedHashMap<String, JoinTable> joinTables;

        /**
         * 结果类型
         */
        private Class<V> resultClass;

        /**
         * 参数
         */
        private Object[] params;

        /**
         * 别名
         */
        private String alias;

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

        {
            this.groupFields = new ArrayList(NumberConstant.INT_TWO);
            BaseDTO.this.orderFields = new ArrayList(NumberConstant.INT_TWO);
            this.joinTables = new LinkedHashMap<>();
        }

        /**
         * 带参构造器
         *
         * @param resultClass 结果类型
         * @param condSize    条件数
         * @param resultSize  结果数
         */
        private FreeQuery(Class<V> resultClass, int condSize, int resultSize) {
            this(resultClass, DEFAULT_TABLE_ALIAS, condSize, resultSize);
        }

        /**
         * 带参构造器
         *
         * @param resultClass 结果类型
         * @param condSize    条件数
         * @param props       结果数
         */
        private FreeQuery(Class<V> resultClass, int condSize, String... props) {
            this(resultClass, DEFAULT_TABLE_ALIAS, condSize, props);
        }

        /**
         * 带参构造器
         *
         * @param resultClass 结果类型
         * @param condSize    条件数
         * @param resultSize  结果数
         */
        private FreeQuery(Class<V> resultClass, String alias, int condSize, int resultSize) {
            this.resultClass = resultClass;
            this.alias = alias;
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
        private FreeQuery(Class<V> resultClass, String alias, int condSize, String... props) {
            this.resultClass = resultClass;
            this.alias = alias;
            this.condFields = new ArrayList<>(condSize);

            if (props != null) {
                this.resultFields = new ArrayList<>(props.length);
                for (String prop : props) {
                    wrapResultField(prop, null);
                }
            }
        }

        /**
         * 内关联
         *
         * @param dto   条件
         * @param alias 别名
         * @return 本身
         */
        public JoinTable join(BaseDTO dto, String alias) {
            return addJoinTable(dto, alias, JoinTypeEnum.INNER);
        }

        /**
         * 左关联
         *
         * @param dto   条件
         * @param alias 别名
         * @return 本身
         */
        public JoinTable lefJoin(BaseDTO dto, String alias) {
            return addJoinTable(dto, alias, JoinTypeEnum.LEFT);
        }

        /**
         * 右关联
         *
         * @param dto   条件
         * @param alias 别名
         * @return 本身
         */
        public JoinTable rightJoin(BaseDTO dto, String alias) {
            return addJoinTable(dto, alias, JoinTypeEnum.RIGHT);
        }

        /**
         * 全关联
         *
         * @param dto   条件
         * @param alias 别名
         * @return 本身
         */
        public JoinTable fullJoin(BaseDTO dto, String alias) {
            return addJoinTable(dto, alias, JoinTypeEnum.FULL);
        }

        /**
         * 添加关联表
         *
         * @param dto   关联条件实体
         * @param alias 别名
         * @return 本身
         */
        private JoinTable addJoinTable(BaseDTO dto, String alias, JoinTypeEnum joinType) {
            JoinTable joinTable = new JoinTable(dto, alias, joinType);
            this.joinTables.put(alias, joinTable);
            return joinTable;
        }

        /**
         * 包裹 属性
         *
         * @param prop    属性名
         * @param wrapper 包装方式
         * @return 本身
         */
        public FreeQuery<V> wrapResultField(String prop, SqlPropWrapperEnum wrapper) {
            return wrapResultField(prop, alias, wrapper);
        }

        /**
         * 包裹 属性
         *
         * @param prop    属性名
         * @param wrapper 包装方式
         * @return 本身
         */
        public FreeQuery<V> wrapResultField(String prop, String alias, SqlPropWrapperEnum wrapper) {
            if (StringUtil.isEmpty(alias) || alias.equals(this.alias)) {
                resultFields.add(new ResultField(prop, alias, wrapper));
            } else {
                resultFields.add(joinTables.get(alias).getDto().new ResultField(prop, alias, wrapper));
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
        public FreeQuery<V> addParamField(String prop, Object value) {
            return addParamField(prop, alias, value, SqlCondWrapperEnum.EQUAL);
        }

        /**
         * 增加参数
         *
         * @param prop  属性
         * @param value 参数
         * @return 本身
         */
        public FreeQuery<V> addParamField(String prop, Object value, SqlCondWrapperEnum wrapper) {
            return addParamField(prop, alias, value, wrapper);
        }

        /**
         * 增加参数
         *
         * @param prop  属性
         * @param value 参数
         * @return 本身
         */
        public FreeQuery<V> addParamField(String prop, String alias, Object value) {
            return addParamField(prop, alias, value, SqlCondWrapperEnum.EQUAL);
        }

        /**
         * 增加参数
         *
         * @param prop  属性
         * @param value 参数
         * @return 本身
         */
        public FreeQuery<V> addParamField(String prop, String alias, Object value, SqlCondWrapperEnum wrapper) {
            FieldValue fieldValue;
            if (StringUtil.isEmpty(alias) || alias.equals(this.alias)) {
                fieldValue = new FieldValue(prop, alias, value, wrapper);
            } else {
                fieldValue = joinTables.get(alias).getDto().new FieldValue(prop, alias, value, wrapper);
            }
            condFields.add(fieldValue);
            return this;
        }

        /**
         * 添加有效限制参数
         *
         * @return 本身
         */
        public FreeQuery<V> addAvailableParam() {
            if (condFields == null) {
                condFields = new ArrayList<>(NumberConstant.INT_TWO);
            }
            condFields.add(new FieldValue(PROP_NAME_OF_DATA_VERSION, alias, NumberConstant.INT_ONE,
                    SqlCondWrapperEnum.GREAT_EQUAL));
            condFields.add(new FieldValue(PROP_NAME_OF_DATA_STATUS, alias, DataStatusEnum.NORMAL.code,
                    SqlCondWrapperEnum.GREAT_EQUAL));
            return this;
        }

        /**
         * 添加分组字段
         *
         * @param prop  分组属性名
         * @param alias 分组属性名
         * @return 本身
         */
        public FreeQuery<V> addGroupBy(String prop, String alias) {
            if (StringUtil.isEmpty(alias) || alias.equals(this.alias)) {
                this.groupFields.add(new GroupField(prop, alias));
            } else {
                this.groupFields.add(joinTables.get(alias).getDto().new GroupField(prop, alias));
            }
            return this;
        }

        /**
         * 添加分组字段
         *
         * @param prop  分组属性名
         * @param alias 分组属性名
         * @return 本身
         */
        public FreeQuery<V> addOrderField(String prop, String alias, String order) {
            if (StringUtil.isEmpty(alias) || alias.equals(this.alias)) {
                BaseDTO.this.orderFields.add(new OrderField(prop, alias, order));
            } else {
                BaseDTO.this.orderFields.add(joinTables.get(alias).getDto().new OrderField(prop, alias, order));
            }
            return this;
        }

        /**
         * 生成SQL文件
         *
         * @return SQL文
         */
        public String generateSql() {
            StringBuilder query = new StringBuilder();

            Iterator<ResultField> rfIterator = resultFields.iterator();
            rfIterator.next().appendTo(query, AppendWayEnum.SELECT.code);
            while (rfIterator.hasNext()) {
                rfIterator.next().appendTo(query, StringConstant.CHAR_COMMA);
            }

            query.append(CharConstant.BLANK).append(AppendWayEnum.FROM.code).append(CharConstant.BLANK)
                    .append(tableName()).append(CharConstant.BLANK).append(alias);
            if (!CollectionUtil.isEmpty(joinTables)) {
                for (Map.Entry<String, JoinTable> entry : joinTables.entrySet()) {
                    entry.getValue().appendTo(query);
                }
            }

            if (!CollectionUtil.isEmpty(condFields)) {
                params = new Object[condFields.size()];

                int j = 0;
                if (condFields.get(0).appendTo(query, AppendWayEnum.WHERE.name())) {
                    params[j++] = condFields.get(0).value;
                }
                for (int i = 1; i < condFields.size(); i++) {
                    if (condFields.get(i).appendTo(query, AppendWayEnum.AND.name())) {
                        params[j++] = condFields.get(i).value;
                    }
                }
            }

            if (!CollectionUtil.isEmpty(groupFields)) {
                Iterator<GroupField> iterator = groupFields.iterator();
                iterator.next().appendTo(query, AppendWayEnum.GROUP_BY.code);
                while (iterator.hasNext()) {
                    iterator.next().appendTo(query, StringConstant.CHAR_COMMA);
                }
            }

            if (!StringUtil.isEmpty(orderBy)) {
                query.append(CharConstant.BLANK).append(AppendWayEnum.ORDER_BY.code)
                        .append(CharConstant.BLANK).append(orderBy);
            } else if (!CollectionUtil.isEmpty(orderFields)) {
                Iterator<OrderField> iterator = orderFields.iterator();
                iterator.next().appendTo(query, AppendWayEnum.ORDER_BY.code);
                while (iterator.hasNext()) {
                    iterator.next().appendTo(query, StringConstant.CHAR_COMMA);
                }
            }
            return query.toString();
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
        private final UpdateTypeEnum updateType;

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
            Iterator<FieldValue> iterator;
            FieldValue next;
            int k = 0;

            switch (updateType) {
                case UPDATE:
                    query.append(AppendWayEnum.UPDATE.code).append(CharConstant.BLANK).append(tableName());
                    params = new Object[valueFields.size() + condFields.size()];

                    iterator = valueFields.iterator();
                    next = iterator.next();
                    if (next.appendTo(query, AppendWayEnum.SET.name())) {
                        params[k++] = next.value;
                    }

                    while (iterator.hasNext()) {
                        next = iterator.next();
                        if (next.appendTo(query, StringConstant.CHAR_COMMA)) {
                            params[k++] = next.value;
                        }
                    }

                    iterator = condFields.iterator();
                    next = iterator.next();
                    if (next.appendTo(query, AppendWayEnum.WHERE.name())) {
                        params[k++] = next.value;
                    }

                    while (iterator.hasNext()) {
                        next = iterator.next();
                        if (next.appendTo(query, AppendWayEnum.AND.name())) {
                            params[k++] = next.value;
                        }
                    }
                    break;
                case INSERT:
                    query.append(AppendWayEnum.INSERT.code).append(CharConstant.BLANK)
                            .append(tableName()).append(CharConstant.OPEN_PAREN);
                    List<Object> paramTemp = new ArrayList<>();

                    iterator = valueFields.iterator();
                    next = iterator.next();
                    paramTemp.add(next.value);
                    query.append(columnOfProp(next.prop));

                    while (iterator.hasNext()) {
                        next = iterator.next();
                        query.append(CharConstant.COMMA).append(columnOfProp(next.prop));
                        paramTemp.add(next.value);
                    }
                    query.append(CharConstant.CLOSE_PAREN).append(AppendWayEnum.VALUES).append(CharConstant.OPEN_PAREN);

                    Iterator<Object> iteratorParam = paramTemp.iterator();
                    Object nextParam = iteratorParam.next();
                    params = new Object[valueFields.size()];
                    if (nextParam == null) {
                        query.append(AppendWayEnum.NULL.code);
                    } else {
                        query.append(CharConstant.QUESTION_MARK);
                        params[k++] = nextParam;
                    }

                    while (iteratorParam.hasNext()) {
                        if (params[0] == null) {
                            query.append(AppendWayEnum.NULL.code);
                        } else {
                            query.append(CharConstant.COMMA).append(CharConstant.QUESTION_MARK);
                            params[k++] = nextParam;
                        }
                    }
                    query.append(CharConstant.CLOSE_PAREN);
                case DELETE:
                    query.append("DELETE FROM ").append(tableName());
                    params = new Object[condFields.size()];

                    if (condFields.get(0).appendTo(query, AppendWayEnum.WHERE.name())) {
                        params[k++] = condFields.get(0).value;
                    }

                    for (int i = 1; i < condFields.size(); i++) {
                        if (condFields.get(i).appendTo(query, AppendWayEnum.AND.name())) {
                            params[k++] = condFields.get(i).value;
                        }
                    }
                    break;
                default:
                    break;
            }

            if (k != params.length) {
                params = Arrays.copyOf(params, k);
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

        /**
         * 增加参数
         *
         * @param prop  属性
         * @param value 参数
         */
        public FreeUpdate addParamField(String prop, Object value, SqlCondWrapperEnum wrapper) {
            condFields.add(new FieldValue(prop, value, wrapper));
            return this;
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
            this.columnAlias = prop;
            this.wrapper = wrapper;
        }

        private ResultField(String prop, String tableAlias, SqlPropWrapperEnum wrapper) {
            this.prop = prop;
            this.tableAlias = tableAlias;
            this.columnAlias = prop;
            this.wrapper = wrapper;
        }

        private ResultField(String prop, String tableAlias, String columnAlias, SqlPropWrapperEnum wrapper) {
            this.prop = prop;
            this.tableAlias = tableAlias;
            this.columnAlias = columnAlias;
            this.wrapper = wrapper;
        }

        /**
         * 属性名称
         */
        String prop;

        /**
         * 别名
         */
        String tableAlias;

        /**
         * 别名
         */
        String columnAlias;

        /**
         * 包装方式
         */
        SqlPropWrapperEnum wrapper;

        /**
         * 全条件值
         *
         * @param query     字符串
         * @param appendWay 拼接方式 SELECT | ,
         * @return 全条件值
         */
        @SneakyThrows
        public void appendTo(Appendable query, String appendWay) {
            if (StringUtil.isNotEmpty(appendWay)) {
                query.append(CharConstant.BLANK).append(appendWay).append(CharConstant.BLANK);
            }

            if (StringUtil.isNotEmpty(tableAlias)) {
                query.append(tableAlias).append(CharConstant.DOT);
            }
            query.append(columnOfProp(prop)).append(" AS ").append(columnAlias);

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

        private FieldValue(String prop, Object value, String alias) {
            this.prop = prop;
            this.value = value;
            this.alias = alias;
        }

        private FieldValue(String prop, Object value, SqlCondWrapperEnum wrapper) {
            this.prop = prop;
            this.value = value;
            this.wrapper = wrapper;
        }

        private FieldValue(String prop, String alias, Object value, SqlCondWrapperEnum wrapper) {
            this.prop = prop;
            this.value = value;
            this.alias = alias;
            this.wrapper = wrapper;
        }

        /**
         * 表别名
         */
        String alias;

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
        @SneakyThrows
        public boolean appendTo(Appendable query, String appendWay) {
            if (StringUtil.isNotEmpty(appendWay)) {
                query.append(CharConstant.BLANK).append(appendWay).append(CharConstant.BLANK);
            }

            if (StringUtil.isNotEmpty(alias)) {
                query.append(alias).append(CharConstant.DOT);
            }
            query.append(columnOfProp(prop));

            if (value == null) {
                query.append(StringConstant.CHAR_EQUAL_MARK).append(AppendWayEnum.IS_NULL.code);
                return false;
            } else {
                if (wrapper == null) {
                    query.append(StringConstant.CHAR_EQUAL_MARK)
                            .append(StringConstant.CHAR_QUESTION_MARK);
                } else {
                    wrapper.wrap(query, StringConstant.CHAR_QUESTION_MARK, null);
                }
                return true;
            }
        }

    }

    /**
     * 排序字段
     *
     * @author Qunhua.Liao
     * @since 2020-05-08
     */
    @Getter
    @Setter
    class GroupField {

        public GroupField() {
        }

        public GroupField(String prop, String alias) {
            if (StringUtil.isEmpty(prop)) {
                throw new ParameterShouldNotEmpty();
            }
            this.prop = prop;
            this.alias = alias;
        }

        /**
         * 属性名
         */
        @ApiModelProperty("属性名")
        private String prop;

        /**
         * 别名
         */
        @ApiModelProperty("别名")
        private String alias;

        /**
         * 全条件值
         *
         * @return 全条件值
         */
        @SneakyThrows
        public void appendTo(Appendable query, String appendWay) {
            if (StringUtil.isNotEmpty(appendWay)) {
                query.append(CharConstant.BLANK).append(appendWay).append(CharConstant.BLANK);
            }

            if (StringUtil.isNotEmpty(alias)) {
                query.append(alias).append(CharConstant.DOT);
            }
            query.append(columnOfProp(prop));
        }

    }

    /**
     * 排序字段
     *
     * @author Qunhua.Liao
     * @since 2020-05-08
     */
    @Getter
    @Setter
    public class OrderField {

        public OrderField() {
        }

        public OrderField(String prop, String order, String alias) {
            if (StringUtil.isEmpty(prop)) {
                throw new ParameterShouldNotEmpty();
            }
            this.prop = prop;
            this.alias = alias;
            this.order = order;
        }

        /**
         * 属性名
         */
        @ApiModelProperty("属性名")
        private String prop;

        /**
         * 别名
         */
        @ApiModelProperty("别名")
        private String alias;

        /**
         * 顺序
         */
        @ApiModelProperty("顺序")
        private String order;

        /**
         * 全条件值
         *
         * @param query     字符串
         * @param appendWay 拼接方式 ORDER BY | ,
         * @return 全条件值
         */
        @SneakyThrows
        public void appendTo(Appendable query, String appendWay) {
            if (StringUtil.isNotEmpty(appendWay)) {
                query.append(CharConstant.BLANK).append(appendWay).append(CharConstant.BLANK);
            }

            if (StringUtil.isNotEmpty(alias)) {
                query.append(alias).append(CharConstant.DOT);
            }
            query.append(columnOfProp(prop));

            if (StringUtil.isNotEmpty(order)) {
                query.append(order);
            }
        }
    }

    @Override
    public String toString() {
        return this.cacheKey();
    }
}
