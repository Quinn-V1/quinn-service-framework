package com.quinn.framework.api.message;

/**
 * 消息模板内容
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageTempContent {

    /**
     * 数据编码（唯一区分）
     *
     * @return 唯一区分
     */
    String sendGroup();

}
