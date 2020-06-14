package com.quinn.framework.compnonent;

import com.quinn.framework.api.message.MessageReceiver;
import com.quinn.framework.api.message.MessageSendRecord;
import com.quinn.framework.model.*;
import com.quinn.framework.service.MessageHelpService;
import com.quinn.framework.util.enums.MessageThreadType;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.enums.LanguageEnum;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
        List<MessageReceiverAdapter> receivers = messageSendParam.getReceivers();
        boolean fastDown = true;
        try {
            if (CollectionUtils.isEmpty(receivers)) {
                BaseResult<List<MessageReceiver>> res = messageHelpService.selectReceivers(
                        messageSendParam.getTemplateKey(), messageSendParam.getMessageType(),
                        messageSendParam.getLangCode()
                );

                if (res.isSuccess()) {
                    List<MessageReceiver> data = res.getData();
                    if (receivers == null) {
                        receivers = new ArrayList<>(res.getData().size());
                        messageSendParam.setReceivers(receivers);
                    }

                    for (MessageReceiver receiver : data) {
                        if (LanguageEnum.by_user.name().equals(receiver.getLangCode())) {
                            fastDown = false;
                        }
                        receivers.add(MessageReceiverAdapter.from(receiver));
                    }
                } else {
                    directMessageInfo.appendError(res.getMessage());
                    return;
                }
            } else {
                for (MessageReceiverAdapter receiver : receivers) {
                    if (StringUtil.isEmptyInFrame(receiver.getLangCode())
                            || LanguageEnum.by_user.name().equals(receiver.getLangCode())) {
                        fastDown = false;
                    }
                }
            }
        } finally {
            if (latchForReceiver != null && fastDown) {
                latchForReceiver.countDown();
            }
        }

        // 以下操作依赖参数解析成发送记录
        if (latchForParam != null) {
            try {
                latchForParam.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
        }

        try {
            List<MessageSendRecord> sendRecords = new ArrayList<>();
            receivers.parallelStream().forEach((receiver) -> {
                BaseResult<List<MessageSendRecord>> sendRecordListResult =
                        MessageInfoFactory.receiver2SendRecord(receiver, messageSendParam.getMessageParam());

                if (sendRecordListResult.isSuccess()) {
                    synchronized (directMessageInfo.getSendRecordListMap()) {
                        sendRecords.addAll(sendRecordListResult.getData());
                    }
                }
            });
            directMessageInfo.addSendRecords(sendRecords);
        } finally {
            if (!fastDown) {
                latchForReceiver.countDown();
            }
        }
    }

    @Override
    public MessageThreadType threadType() {
        return MessageThreadType.RECEIVER;
    }

}
