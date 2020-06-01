package com.quinn.framework.model;

import com.quinn.framework.api.message.MessageReceiver;

/**
 * 默认消息接受者
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public class DefaultMessageReceiver implements MessageReceiver {
    @Override
    public void setReceiverType(String receiverType) {

    }

    @Override
    public void setReceiverValue(String receiverValue) {

    }

    @Override
    public String instanceKey() {
        return null;
    }
}
