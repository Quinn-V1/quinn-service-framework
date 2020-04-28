package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

import java.util.Map;

/**
 * 通用参数业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-28
 */
public interface ParamService {

    /**
     * 获取指定参数
     *
     * @param paramType   参数类型
     * @param paramName   参数名称
     * @param primaryKey  参数主键
     * @param subKey      参数子键
     * @param grandSubKey 参数孙键
     * @return 参数值
     */
    BaseResult<String> getParamValue(String paramType, String paramName, String primaryKey, String subKey, String grandSubKey);

    /**
     * 获取指定类型的所有参数
     *
     * @param paramType   参数类型
     * @param primaryKey  参数主键
     * @param subKey      参数子键
     * @param grandSubKey 参数孙键
     * @return Map<参数名, 参数值>
     */
    BaseResult<Map<String, String>> getParamMap(String paramType, String primaryKey, String subKey, String grandSubKey);

    /**
     * 获取指定参数
     *
     * @param paramType   参数类型
     * @param paramName   参数名称
     * @param primaryKeys  参数主键
     * @param subKeys      参数子键
     * @param grandSubKeys 参数孙键
     * @return 参数值
     */
    BaseResult<String> getClosestParamValue(
            String paramType, String paramName, String[] primaryKeys,String[] subKeys, String[] grandSubKeys);

    /**
     * 获取指定类型的所有参数
     *
     * @param paramType   参数类型
     * @param primaryKeys  参数主键
     * @param subKeys      参数子键
     * @param grandSubKeys 参数孙键
     * @return Map<参数名, 参数值>
     */
    BaseResult<Map<String, String>> getClosestParamMap(
            String paramType, String[] primaryKeys, String[] subKeys, String[] grandSubKeys);


}
