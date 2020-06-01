package com.quinn.framework.compnonent;

import com.quinn.framework.api.message.MessageHelpService;
import com.quinn.framework.api.message.MessageReceiver;
import com.quinn.framework.api.message.MessageSendRecord;
import com.quinn.framework.model.DirectMessageInfo;
import com.quinn.framework.model.MessageSendParam;
import com.quinn.framework.model.MessageThread;
import com.quinn.framework.util.enums.ThreadType;
import com.quinn.util.base.model.BaseResult;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 收件对象转换为直接发件地址
 *
 * @author Qunhua.Liao
 * @since 2020-02-08
 */
@Setter
public class ReceiverToDirect extends MessageThread {

    /**
     * 消息模板业务操作接口
     */
    private MessageHelpService messageHelpService;

    public ReceiverToDirect(DirectMessageInfo messageInfo, MessageSendParam messageSendParam,
                            MessageHelpService messageHelpService) {
        this.directMessageInfo = messageInfo;
        this.messageSendParam = messageSendParam;
        this.messageHelpService = messageHelpService;
    }

    @Override
    public void handle() {
        List<MessageReceiver> receivers = messageSendParam.getReceivers();
        if (CollectionUtils.isEmpty(receivers)) {
            try {
                BaseResult<List<MessageReceiver>> res = messageHelpService.selectReceivers(
                        messageSendParam.getTemplateKey(), messageSendParam.getMessageType(),
                        messageSendParam.getLanguageCode()
                );

                if (res.isSuccess()) {
                    receivers = res.getData();
                    messageSendParam.setReceivers(receivers);
                } else {
                    directMessageInfo.appendError(res.getMessage());
                    return;
                }
            } finally {
                if (latchForContentReceiver != null) {
                    latchForContentReceiver.countDown();
                }
            }
        } else {
            if (latchForContentReceiver != null) {
                latchForContentReceiver.countDown();
            }
        }

        if (latchForReceiver != null) {
            try {
                latchForReceiver.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
        }

        receivers.parallelStream().forEach((receiver) -> {
            BaseResult<List<MessageSendRecord>> sendRecordListResult =
                    messageHelpService.receiver2SendRecord(receiver, messageSendParam.getMessageParam());

            if (sendRecordListResult.isSuccess()) {
                synchronized (directMessageInfo.getSendRecordListMap()) {
                    directMessageInfo.addSendRecords(sendRecordListResult.getData());
                }
            } else {
                directMessageInfo.appendError(sendRecordListResult.getMessage());
            }
        });
    }

    @Override
    public ThreadType threadType() {
        return ThreadType.RECEIVER;
    }

}
