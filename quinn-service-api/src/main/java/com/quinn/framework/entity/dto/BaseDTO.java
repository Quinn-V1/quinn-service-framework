package com.quinn.framework.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 基础数据传输类
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
@Setter
@Getter
public class BaseDTO<T> {

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
     * 设置实体类对象
     *
     * @param <V>           结果泛型
     * @param entityClass   实体类对象
     * @return              本身
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
     * 获取缓存键
     *
     * @return
     */
    public String cacheKey() {
        if (entityClass == null) {
            return null;
        }
        return entityClass.getSimpleName() + ":" + dataKey();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":" + dataKey();
    }

}
