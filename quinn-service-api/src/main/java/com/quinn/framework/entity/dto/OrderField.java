package com.quinn.framework.entity.dto;

import com.quinn.util.base.exception.ParameterShouldNotEmpty;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.enums.OrderByTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 排序字段
 *
 * @author Qunhua.Liao
 * @since 2020-05-08
 */
@Getter
@Setter
public class OrderField {

    /**
     * 默认表别名
     */
    public static final String DEFAULT_TABLE_ALIAS = "t";

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
     * 获取表别名（涉及默认值）
     *
     * @return 表别名
     */
    public String ofAlias() {
        return StringUtil.isEmpty(alias) ? DEFAULT_TABLE_ALIAS : alias;
    }

    /**
     * 获取排序：涉及默认值和清洗
     *
     * @return 排序
     */
    public String ofOrder() {
        if (StringUtil.isEmpty(order)) {
            return OrderByTypeEnum.DEFAULT_ORDER;
        }

        OrderByTypeEnum orderByTypeEnum = OrderByTypeEnum.valueOf(order.toUpperCase());
        return orderByTypeEnum.name();
    }
}
