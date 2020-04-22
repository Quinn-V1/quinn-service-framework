package com.quinn.framework.entity.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quinn.framework.api.entityflag.ParameterAble;
import lombok.Getter;
import lombok.Setter;

/**
 * 可配置的实体
 *
 * @author Qunhua.Liao
 * @since 2020-04-22
 */
@Getter
@Setter
public class ParameterAbleDO extends BaseDO implements ParameterAble {

    /**
     * 综合数据库参数
     */
    @JsonIgnore
    private String coDbParamValue;

    @Override
    public String toString() {
        return super.toString();
    }

}
