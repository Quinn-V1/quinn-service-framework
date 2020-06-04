package com.quinn.framework.model;

import com.quinn.framework.api.message.MessageSendRecord;
import com.quinn.framework.api.message.MessageSender;
import com.quinn.framework.api.message.MessageSenderSupplier;
import com.quinn.framework.api.message.MessageServer;
import com.quinn.framework.service.MessageHelpService;
import com.quinn.framework.util.MessageInfoUtil;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.model.BaseResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息发送服务创建工厂
 *
 * @author Qunhua.Liao
 * @since 2020-06-01
 */
public class MessageSenderFactory {

    /**
     * 关联数据库的协助类
     */
    private static MessageHelpService messageHelpService;

    /**
     * 发送服务提供者
     */
    private static Map<String, MessageSenderSupplier> messageSenderSupplierMap = new HashMap<>();

    /**
     * 按照消息服务编码进行映射
     */
    private static Map<String, MessageSender> messageSenderMap = new HashMap<>();

    /**
     * 按照消息类型进行映射
     */
    private static Map<String, List<MessageSender>> messageSenderListMap = new HashMap<>();


    /**
     * 根据发送对象找发送对象
     *
     * @param sendRecord 发送记录
     * @return 发送对象
     */
    public static MessageSender findMessageSender(MessageSendRecord sendRecord) {
        String serverKey = sendRecord.getServerKey();
        if (StringUtil.isEmptyInFrame(serverKey)) {
            MessageSender messageSender = messageSenderMap.get(serverKey);
            if (messageSender != null) {
                return messageSender;
            }

            BaseResult<MessageServer> serverRes = messageHelpService.getMessageServerByKey(serverKey);
            if (serverRes.isSuccess()) {
                MessageSenderSupplier supplier = messageSenderSupplierMap.get(sendRecord.getMessageType());
                if (supplier == null) {
                    // FIXME
                    throw new BaseBusinessException();
                }
                BaseResult<MessageSender> senderRes = supplier.create(serverRes.getData());
                if (!senderRes.isSuccess()) {
                    // FIXME
                    throw new BaseBusinessException();
                }

                messageSender = senderRes.getData();
                messageSenderMap.put(serverKey, messageSender);
                CollectionUtil.nullSafePutList(messageSenderListMap, messageSender, messageSender.subKey());
                return messageSender;
            }
        }

        String subKey = sendRecord.subKey();
        List<MessageSender> messageSenders = messageSenderListMap.get(subKey);
        if (!CollectionUtil.isEmpty(messageSenders)) {
            return messageSenders.get(0);
        }

        BaseResult<List<MessageServer>> res = messageHelpService.selectMessageServerBySubKey(subKey);
        if (res.isSuccess()) {
            // subKey 涵盖MessageType
            MessageSenderSupplier supplier = messageSenderSupplierMap.get(sendRecord.getMessageType());
            if (supplier == null) {
                // FIXME
                throw new BaseBusinessException();
            }

            List<MessageServer> messageServers = res.getData();
            messageSenders = new ArrayList<>(messageSenders.size());

            for (MessageServer server : messageServers) {
                BaseResult<MessageSender> senderRes = supplier.create(server);
                if (!senderRes.isSuccess()) {
                    continue;
                }
                messageSenders.add(senderRes.getData());
            }

            if (messageSenders.size() > 0) {
                return messageSenders.get(0);
            }
        }

        // 初始加载所有默认消息队列
        String messageType = sendRecord.getMessageType();
        String defaultSubKey = MessageInfoUtil.defaultServerSubKey(messageType);
        messageSenders = messageSenderListMap.get(defaultSubKey);
        if (CollectionUtil.isEmpty(messageSenders)) {
            throw new BaseBusinessException();
        }

        messageSenderListMap.put(subKey, messageSenders);
        return messageSenders.get(0);
    }

    /**
     * 设置消息数据库信息获取帮助业务对象
     *
     * @param messageHelpService 消息数据库信息获取
     */
    public static void setMessageServerService(MessageHelpService messageHelpService) {
        MessageSenderFactory.messageHelpService = messageHelpService;
    }

}
