package com.quinn.framework.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 具有热度的数据
 *
 * @author Qunhua.Liao
 * @since 2020-05-24
 */
@Getter
@Setter
public class HeatRateObject<T> {

    public HeatRateObject(T data, int heatRate) {
        this.data = data;
        this.heatRate = heatRate;
    }

    /**
     * 数据
     */
    private T data;

    /**
     * 热度
     */
    private int heatRate;

}
