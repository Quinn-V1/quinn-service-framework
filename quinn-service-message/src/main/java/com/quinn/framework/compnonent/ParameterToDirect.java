package com.quinn.framework.compnonent;

import com.quinn.framework.model.DirectMessageInfo;
import com.quinn.framework.model.MessageSendParam;
import com.quinn.framework.model.MessageThread;
import com.quinn.framework.service.MessageHelpService;
import com.quinn.framework.util.enums.MessageThreadType;
import com.quinn.util.base.model.BaseResult;
import lombok.Setter;

import java.util.Map;

/**
 * 参数直接化线程
 *
 * @author QUnhua.Liao
 * @since 2020-02-11
 */
@Setter
public class ParameterToDirect extends MessageThread {

    /**
     * 消息模板业务操作接口
     */
    private MessageHelpService messageHelpService;

    public ParameterToDirect(DirectMessageInfo messageInfo, MessageSendParam messageSendParam,
                             MessageHelpService messageHelpService) {
        this.directMessageInfo = messageInfo;
        this.messageSendParam = messageSendParam;
        this.messageHelpService = messageHelpService;
    }

    @Override
    public void handle() {
        try {
            BaseResult<Map<String, Object>> invoke = messageHelpService.fillRunTimeParam(
                    messageSendParam.getMessageParam());

            if (invoke.isSuccess()) {
                messageSendParam.setMessageParam(invoke.getData());
            }
        } finally {
            if (latchForParam != null) {
                latchForParam.countDown();
            }
        }
    }

    @Override
    public MessageThreadType threadType() {
        return MessageThreadType.PARAM;
    }
}
