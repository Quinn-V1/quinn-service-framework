package com.quinn.framework.api.entityflag;

/**
 * 可参数配置化标识
 *
 * @author Qunhua.Liao
 * @since 2020-03-28
 */
public interface ParameterAble {

    /**
     * 获取参数键
     *
     * @return  参数键
     */
    String cacheKey();

    /**
     * 获取参数值
     *
     * @return  参数值
     */
    default String getParamValue(){return null;}

    /**
     * 获取参数值
     *
     * @param paramValue    参数值
     */
    default void setParamValue(String paramValue) {}

}
