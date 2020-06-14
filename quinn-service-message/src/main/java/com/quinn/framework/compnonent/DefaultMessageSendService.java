package com.quinn.framework.compnonent;

import com.quinn.framework.api.message.MessageSendRecord;
import com.quinn.framework.api.message.MessageSender;
import com.quinn.framework.model.MessageSenderFactory;
import com.quinn.framework.service.MessageSendService;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.model.BaseResult;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 默认消息发送服务，消息类型分发
 *
 * @author Qunhua.Liao
 * @since 2020-06-01
 */
public class DefaultMessageSendService implements MessageSendService {

    @Resource
    private ScheduledExecutorService messageExecutorService;

    @Override
    public BaseResult sendAll(List<MessageSendRecord> sendRecordList) {
        // Key为发送服务对象；键为发送消息记录
        Map<MessageSender, List<MessageSendRecord>> messageSendRecordListMap =
                CollectionUtil.collectionToListMap(sendRecordList, messageSendRecord ->
                        MessageSenderFactory.findMessageSender(messageSendRecord));

        for (Map.Entry<MessageSender, List<MessageSendRecord>> entry : messageSendRecordListMap.entrySet()) {
            messageExecutorService.execute(() -> {
                entry.getKey().sendAll(entry.getValue());
            });
        }

        return BaseResult.SUCCESS;
    }

}
