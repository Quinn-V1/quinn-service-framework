package com.quinn.framework.api;

/**
 * 同步监听器
 *
 * @author Quinhua.Liao
 * @since 2020-5-26
 */
public interface ConcurrentMqListener extends MqListener {

    /**
     * 获取监听器在监听列表中的位置
     *
     * @return 顺序
     */
    int getConcurrent();

}
