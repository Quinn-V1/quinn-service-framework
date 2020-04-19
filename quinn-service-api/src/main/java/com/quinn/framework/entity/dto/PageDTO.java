package com.quinn.framework.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页数据传输类
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
@Setter
@Getter
public class PageDTO<T> extends BaseDTO {

    /**
     * 当前页
     */
    @ApiModelProperty("当前页数")
    private Integer pageNum;

    /**
     * 每页数量
     */
    @ApiModelProperty("每页显示数量")
    private Integer pageSize;

    @Override
    public String toString() {
        return super.toString();
    }

}
