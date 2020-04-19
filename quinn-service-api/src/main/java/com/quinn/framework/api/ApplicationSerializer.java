package com.quinn.framework.api;

import java.io.IOException;

/**
 * 容器序列化工具
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
public interface ApplicationSerializer {

    /**
     * 将对象序列化为byte数组
     *
     * @param o 序列化对象
     * @return byte数组
     * @throws IOException
     */
    byte[] serialize(Object o);

    /**
     * 将byte数组反序列化为对象
     *
     * @param data byte数组
     * @param tpl 泛型类对象
     * @param <T> 泛型
     * @return 反序列化对象
     * @throws IOException
     */
    <T> T deserialize(byte[] data, Class<T> tpl);

    /**
     * 将byte数组反序列化为对象
     *
     * @param data byte数组
     * @return 反序列化对象
     * @throws IOException
     */
    Object deserialize(byte[] data);

    /**
     * 将对象转化为字符串
     *
     * @param o 对象
     * @return Json或者XML字符串
     * @throws IOException
     */
    String serializeToStr(Object o);

    /**
     * 将字符串转化为对象
     *
     * @param data 字符串：Json或者Xml
     * @param tpl 对象类型
     * @param <T> 范型
     * @return 对象
     * @throws IOException
     */
    <T> T deserializeFromStr(String data, Class<T> tpl);
}
