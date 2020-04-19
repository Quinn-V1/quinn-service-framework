package com.quinn.framework.api.message;

import com.alibaba.fastjson.JSONObject;
import com.quinn.util.base.model.BaseResult;

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
    BaseResult send(MessageInfo messageInfo);

    /**
     * 测试连接
     *
     * @return
     */
    BaseResult test();

}
