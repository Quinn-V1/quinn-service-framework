package com.quinn.framework.api.message;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息服务器
 *
 * @author Qunhua.Liao
 * @since 2020-06-02
 */
public interface MessageServer {

    /**
     * 次级主键(消息类型 + 租户)
     * 不同消息类型，服务配置肯定不一样，碰租户的场景：不同租户可能使用不一样的，也可能使用通用的（ALL_OF_DATA）
     *
     * @return 次级主键
     * @see MessageServer
     */
    String subKey();

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    String getMessageType();

    /**
     * 服务编码
     *
     * @return 服务编码
     */
    String getServerKey();

    /**
     * 获取连接参数
     *
     * @return 连接参数
     */
    JSONObject getConnectParam();

}
