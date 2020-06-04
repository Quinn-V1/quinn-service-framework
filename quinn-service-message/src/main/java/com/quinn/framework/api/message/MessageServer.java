package com.quinn.framework.api.message;

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
     * @see MessageServer
     * @return 次级主键
     */
    String subKey();

}
