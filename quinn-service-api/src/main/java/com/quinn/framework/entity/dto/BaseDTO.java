package com.quinn.framework.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quinn.framework.util.enums.WrapperEnum;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.util.StringUtil;
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

    {
        // 默认不做查询条数限制
        limit = -1;

        // 默认查找有效数据
        dataVersionFrom = 1;
    }

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
     * 操作者
     */
    @ApiModelProperty("操作者")
    private String operator;

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
     * 归档标识
     */
    @ApiModelProperty("归档标识")
    private Integer archiveFlag;

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
        return entityClass.getSimpleName() + ":" + dataKey();
    }

    /**
     * 逆向解析缓存键
     */
    public void cacheKey(String cacheKey) {
        if (entityClass != null) {
            cacheKey = cacheKey.substring(entityClass.getSimpleName().length() + 1);
        }
        dataKey(cacheKey);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":" + dataKey();
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
     * @author Qunhua.Liao
     * @since 2020-04-24
     */
    public class FreeQuery {

        /**
         * 带参构造器
         *
         * @param resultClass   结果类型
         * @param condSize      条件数
         * @param resultSize    结果数
         */
        private FreeQuery (Class resultClass, int condSize, int resultSize) {
            this.resultClass = resultClass;
            this.condFields = new ArrayList<>(condSize);
            this.resultFields = new ArrayList<>(resultSize);
        }

        /**
         * 条件字段
         */
        private List<CondField> condFields;

        /**
         * 结果字段
         */
        private List<ResultField> resultFields;

        /**
         * 结果类型
         */
        private Class resultClass;

        /**
         * 参数
         */
        private Object[] params;

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
            query.append(" FROM ").append(tableName()).append(" WHERE ");

            params = new Object[condFields.size()];
            for (int i = 0; i < condFields.size(); i++) {
                CondField condField = condFields.get(i);
                query.append(columnOfProp(condField.prop)).append(" = ? AND ");
                params[i] = condField.value;
            }

            query.delete(query.length() - 5, query.length());
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
         * 包裹 属性
         *
         * @param prop
         * @param wrapper
         */
        public void wrapResultField(String prop, WrapperEnum wrapper) {
            resultFields.add(new ResultField(prop, wrapper));
        }

        public void addParamField(String prop, WrapperEnum wrapper) {
            resultFields.add(new ResultField(prop, wrapper));
        }
    }

    /**
     * 条件字段
     *
     * @author Qunhua.Liao
     * @since 2020-04-24
     */
    private class CondField {

        private CondField(String prop, Object value) {
            this.prop = prop;
            this.value = value;
        }

        /**
         * 属性名称
         */
        String prop;

        /**
         * 条件值
         */
        Object value;

    }

    /**
     * 结果字段
     *
     * @author Qunhua.Liao
     * @since 2020-04-24
     */
    private class ResultField {

        private ResultField(String prop, WrapperEnum wrapper) {
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
        WrapperEnum wrapper;

        /**
         * 全名
         *
         * @return 全名
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
