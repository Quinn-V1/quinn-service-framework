package com.quinn.framework.api.message;

import com.quinn.framework.model.MessageSendParam;
import com.quinn.util.base.model.BaseResult;

/**
 * 消息对外接口业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageApiService {

    /**
     * 消息发送接口
     *
     * @param messageSendParam 消息发送参数
     * @return 消息发送结果
     */
    BaseResult send(MessageSendParam messageSendParam);

    /**
     * 消息预览接口
     *
     * @param messageSendParam 消息发送参数
     * @return 消息发送结果
     */
    BaseResult preview(MessageSendParam messageSendParam);

}
