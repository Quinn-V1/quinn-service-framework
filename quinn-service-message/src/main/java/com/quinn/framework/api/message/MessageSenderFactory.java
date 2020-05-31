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
public interface MessageSenderFactory {

    /**
     * Bean名称前缀
     */
    String BEAN_NAME_PREFIX = "messageSenderFactory";

    /**
     * 创建消息发送器
     *
     * @param jsonObject    参数
     * @return              消息发送器
     */
    BaseResult<MessageSender> create(JSONObject jsonObject);

    /**
     * 连接参数示例
     *
     * @return  连接参数示例
     */
    BaseResult<Map> connectParamExample();

}
