package com.quinn.framework.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页信息
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
@Setter
@Getter
public class PageInfo<T> {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页面", example = "1")
    private int pageNum;

    /**
     * 每页显示数量
     */
    @ApiModelProperty(value = "每页显示数量", example = "10")
    private int pageSize;

    /**
     * 总数量
     */
    @ApiModelProperty(value = "总数量", example = "520")
    private long total;

    /**
     * 当前页数据
     */
    @ApiModelProperty(value = "当前页数据")
    private List<T> dataList;

}
