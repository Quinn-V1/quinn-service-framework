package com.quinn.framework.service;

import com.quinn.framework.api.message.*;
import com.quinn.util.base.model.BaseResult;

import java.util.List;
import java.util.Map;

/**
 * 消息帮助类接口（用于向数据库请求）
 * 解析消息模板、服务、消息服务、收件人信息等等
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public interface MessageHelpService {

    /**
     * 根据业务主键查找消息实例
     *
     * @param fromSystem  来源系统
     * @param businessKey 消息实例
     * @return
     */
    <T extends MessageInstance> BaseResult<T> getInstanceByBiz(String fromSystem, String businessKey);

    /**
     * 通过编码获取消息模板
     *
     * @param templateKey 模板编码
     * @return 消息模板
     */
    BaseResult<MessageTemp> getTemplateByKey(String templateKey);

    /**
     * 查找消息内容
     *
     * @param templateKey  消息模板
     * @param messageType  消息类型
     * @param languageCode 语言编码
     * @return
     */
    BaseResult<List<MessageTempContent>> selectContents(String templateKey, String messageType, String languageCode);

    /**
     * 根据编码获取消息服务
     *
     * @param serverKey 服务编码
     * @return 查找结果
     */
    BaseResult<MessageServer> getMessageServerByKey(String serverKey);

    /**
     * 根据次级主键查找
     *
     * @param subKey 次级主键
     * @return 消息服务列表
     */
    BaseResult<List<MessageServer>> selectMessageServerBySubKey(String subKey);

    /**
     * 填充运行时参数
     *
     * @param messageParam 参数模板
     * @return 运行时参数
     */
    BaseResult<Map<String, Object>> fillRunTimeParam(Map<String, Object> messageParam);

    /**
     * 查找消息收件对象
     *
     * @param templateKey  消息模板
     * @param messageType  消息类型
     * @param languageCode 语言编码
     * @return
     */
    BaseResult<List<MessageReceiver>> selectReceivers(String templateKey, String messageType, String languageCode);

    /**
     * 发送对象转发送记录
     *
     * @param receiver     收件对象
     * @param messageParam 发送记录
     * @return 发送记录
     */
    BaseResult<List<MessageSendRecord>> receiver2SendRecord(MessageReceiver receiver, Map<String, Object> messageParam);

    /**
     * 保存消息实例
     *
     * @param instance 消息实例
     * @return 保存结果
     */
    BaseResult saveInstance(MessageInstance instance);

    /**
     * 保存发送记录
     *
     * @param sendRecord 发送记录
     * @return 保存结果
     */
    BaseResult saveSendRecord(MessageSendRecord sendRecord);

}
