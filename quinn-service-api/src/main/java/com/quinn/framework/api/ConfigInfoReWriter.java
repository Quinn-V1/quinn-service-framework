package com.quinn.framework.api;

import java.util.Properties;
import java.util.Set;

/**
 * 配置信息重写器
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public interface ConfigInfoReWriter {

    /**
     * 获取顺序
     *
     * @return  顺序
     */
    public int getPriority();

    /**
     * 影响的键
     *
     * @return  影响的键
     */
    Set<String> effectedKeys();

    /**
     * 重写配置：解密
     *
     * @param properties    配置信息
     */
    void encrypt(Properties properties);

    /**
     * 重写配置：解密
     *
     * @param properties    配置信息
     */
    void decrypt(Properties properties);

    /**
     * 重写配置：解密
     *
     * @param value    配置信息
     * @return 加密后的值
     */
    String encryptValue(String value);

    /**
     * 重写配置：解密
     *
     * @param value    配置信息
     * @return 解密后的值
     */
    String decryptValue(String value);

    /**
     * 匹配值
     *
     * @param key   键
     * @return      是否匹配
     */
    default boolean keyMatches(Object key) {return false;}

    /**
     * 匹配值
     *
     * @param value 值
     * @return      是否匹配
     */
    default boolean valueMatches(Object value) {return false;}

}
