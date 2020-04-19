package com.quinn.framework.api;

/**
 * 自定义序列化对象
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
public interface CustomSerializable {

    /**
     * 自定义序列化
     *
     * @return 自定义序列化对象
     */
    Object customSerialize();

}
