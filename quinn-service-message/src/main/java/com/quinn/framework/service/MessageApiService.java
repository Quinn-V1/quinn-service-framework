package com.quinn.framework.service;

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

    /**
     * 消息预览接口
     *
     * @param sendRecordIds 消息发送ID
     * @return 消息发送结果
     */
    BaseResult revokeBySendIds(Long[] sendRecordIds);

    /**
     * 消息预览接口
     *
     * @param instIds 消息发送ID
     * @return 消息发送结果
     */
    BaseResult revokeByInstIds(Long[] instIds);

    /**
     * 消息预览接口
     *
     * @param batchKey 批次号
     * @return 消息发送结果
     */
    BaseResult revokeByBathNo(String batchKey);

}
