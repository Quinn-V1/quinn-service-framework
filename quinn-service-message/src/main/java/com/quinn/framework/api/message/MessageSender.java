package com.quinn.framework.api.message;

import com.alibaba.fastjson.JSONObject;
import com.quinn.util.base.model.BaseResult;

import java.util.List;

/**
 * 消息发送器
 *
 * @author Qunhua.Liao
 * @since 2020-04-07
 */
public interface MessageSender {

    /**
     * 初始化
     *
     * @param params 参数
     * @return 初始化是否成功
     */
    BaseResult init(JSONObject params);

    /**
     * 发送消息
     *
     * @param sendRecord 消息发送记录
     * @return 发送是否成功
     */
    BaseResult send(MessageSendRecord sendRecord);

    /**
     * 测试连接
     *
     * @return 发送是否成功
     */
    BaseResult test();

    /**
     * 发送多条消息
     *
     * @param sendRecords 发送记录
     * @return 发送结果
     */
    BaseResult sendAll(List<MessageSendRecord> sendRecords);

}
