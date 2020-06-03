package com.quinn.framework.api.message;

import java.util.Set;

/**
 * 消息实例
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageInstance {

    /**
     * 设置来源系统
     *
     * @param fromSystem 来源系统
     */
    void setFromSystem(String fromSystem);

    /**
     * 设置业务主键
     *
     * @param businessKey 业务主键
     */
    void setBusinessKey(String businessKey);

    /**
     * 设置消息批次号（表示一批消息是同次发送的）
     *
     * @param batchKey 批次号
     */
    void setBatchKey(String batchKey);

    /**
     * 设置消息类型
     *
     * @param messageType 消息类型
     */
    void setMessageType(String messageType);

    /**
     * 设置语言编码
     *
     * @param langCode 语言编码
     */
    void setLangCode(String langCode);

    /**
     * 数据编码
     *
     * @return 数据编码
     */
    String sendGroup();

    /**
     * 获取消息主题
     *
     * @return 消息主题
     */
    String getSubject();

    /**
     * 设置主题
     *
     * @param subject 主题
     */
    void setSubject(String subject);

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    String getContent();

    /**
     * 设置消息内容
     *
     * @param content 消息内容
     */
    void setContent(String content);

    /**
     * 获取消息附件
     *
     * @return 消息附件
     */
    String getAttachment();

    /**
     * 设置消息附件
     *
     * @param attachment 附件
     */
    void setAttachment(String attachment);

    /**
     * 设置消息连接
     *
     * @param url 消息连接
     */
    void setMsgUrl(String url);

    /**
     * 设置消息链接
     *
     * @return
     */
    String getMsgUrl();

    /**
     * 设置发件人
     *
     * @param sender 发件人
     */
    void setSender(String sender);

    /**
     * 获取收件人信息
     *
     * @return 收件人
     */
    Set<String> getReceiverAddresses();

}
