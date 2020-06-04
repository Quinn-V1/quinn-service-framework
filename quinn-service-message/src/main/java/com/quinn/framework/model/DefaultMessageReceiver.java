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
    public String getReceiverType() {
        return null;
    }

    @Override
    public String getReceiverValue() {
        return null;
    }

    @Override
    public void setReceiverType(String receiverType) {

    }

    @Override
    public void setReceiverValue(String receiverValue) {

    }

    @Override
    public String sendGroup() {
        return null;
    }

    @Override
    public String getMessageType() {
        return null;
    }

    @Override
    public String getLangCode() {
        return null;
    }

    @Override
    public String getServerKey() {
        return null;
    }

    @Override
    public Integer getUrgentLevel() {
        return null;
    }
}
