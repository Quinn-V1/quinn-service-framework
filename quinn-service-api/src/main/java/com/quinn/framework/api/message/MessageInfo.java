package com.quinn.framework.api.message;

import java.util.Set;

/**
 * 消息信息
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public interface MessageInfo {

    /**
     * 获取消息主题
     *
     * @return 消息主题
     */
    String getMessageSubject();

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    String getMessageContent();

    /**
     * 获取消息附件
     *
     * @return 消息附件
     */
    String getMessageAttachment();

    /**
     * 获取收件人信息
     *
     * @return 收件人
     */
    Set<String> getReceiverAddress();

}
