package com.quinn.framework.service;

import com.quinn.framework.api.message.MessageSendRecord;
import com.quinn.util.base.model.BaseResult;

import java.util.List;

/**
 * 消息发送服务
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageSendService {

    /**
     * 发送消息列表
     *
     * @param sendRecordList 发送记录
     * @return 发送结果
     */
    BaseResult sendAll(List<MessageSendRecord> sendRecordList);

}
