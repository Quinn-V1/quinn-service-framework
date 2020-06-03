package com.quinn.framework.api.message;

import com.alibaba.fastjson.JSONObject;
import com.quinn.util.base.model.BaseResult;

import java.util.Map;

/**
 * 消息发送器生成工厂
 *
 * @author Qunhua.Liao
 * @since 2020-04-07
 */
public interface MessageSenderSupplier {

    /**
     * Bean名称前缀
     */
    String BEAN_NAME_PREFIX = "messageSenderFactory";

    /**
     * 创建消息发送器
     *
     * @param jsonObject 参数
     * @return 消息发送器
     */
    BaseResult<MessageSender> create(JSONObject jsonObject);

    /**
     * 连接参数示例
     *
     * @return 连接参数示例
     */
    BaseResult<Map> connectParamExample();

    /**
     * 创建发送服务
     *
     * @param messageServer 发送服务参数
     * @return 发送服务
     */
    BaseResult<MessageSender> create(MessageServer messageServer);

}
