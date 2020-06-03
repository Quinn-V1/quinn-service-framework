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
     * @return
     */
    BaseResult init(JSONObject params);

    /**
     * 发送消息
     *
     * @param messageInfo
     * @return
     */
    BaseResult send(MessageInstance messageInfo);

    /**
     * 测试连接
     *
     * @return
     */
    BaseResult test();

    /**
     * 发送多条消息
     *
     * @param sendRecords 发送记录
     * @return 发送结果
     */
    BaseResult sendAll(List<MessageSendRecord> sendRecords);

    /**
     * 次级主键区分（一般使用类型：租户）
     *
     * @return 没有指定serverKey, 则按照此优先级进行选择
     */
    String subKey();

}
