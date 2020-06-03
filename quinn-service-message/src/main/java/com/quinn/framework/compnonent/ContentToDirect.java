package com.quinn.framework.compnonent;

import com.quinn.framework.api.message.MessageInstance;
import com.quinn.framework.api.message.MessageReceiver;
import com.quinn.framework.api.message.MessageTempContent;
import com.quinn.framework.model.DirectMessageInfo;
import com.quinn.framework.model.MessageInfoFactory;
import com.quinn.framework.model.MessageSendParam;
import com.quinn.framework.model.MessageThread;
import com.quinn.framework.service.MessageHelpService;
import com.quinn.framework.util.enums.ThreadType;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.model.BaseResult;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 内容具体化线程
 *
 * @author Qunhua.Liao
 * @since 2020-02-11
 */
@Setter
public class ContentToDirect extends MessageThread {

    /**
     * 消息模板业务操作接口
     */
    private MessageHelpService messageHelpService;

    public ContentToDirect(DirectMessageInfo messageInfo, MessageSendParam messageSendParam,
                           MessageHelpService messageHelpService) {
        this.directMessageInfo = messageInfo;
        this.messageSendParam = messageSendParam;
        this.messageHelpService = messageHelpService;
    }

    @Override
    public void handle() {
        String templateKey = messageSendParam.getTemplateKey();
        String messageType = messageSendParam.getMessageType();
        String languageCode = messageSendParam.getLangCode();

        try {
            BaseResult<List<MessageTempContent>> contentRes =
                    messageHelpService.selectContents(templateKey, messageType, languageCode);

            if (!contentRes.isSuccess()) {
                // FIXME
                directMessageInfo.appendError(contentRes.getMessage());
                return;
            }

            Map<String, MessageTempContent> tempContents = CollectionUtil.collectionToMap(contentRes.getData(),
                    messageTempContent -> messageTempContent.sendGroup());

            // 如果消息类型为空、或者语言编码为空：则需要根据收件人信息解析出来需要哪些（消息类型 + 语言）
            if (StringUtil.isEmptyInFrame(messageType) || StringUtil.isEmptyInFrame(languageCode)) {
                try {
                    latchForReceiver.await(30, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                }

                BaseResult res = filter(tempContents, messageSendParam.getReceivers());
                if (!res.isSuccess()) {
                    directMessageInfo.appendError(res.getMessage());
                    return;
                }
            }

            // 以下操作依赖参数将模板解析成具体内容
            if (!CollectionUtils.isEmpty(tempContents)) {
                try {
                    if (latchForParam != null) {
                        latchForParam.await(30, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e) {
                }

                try {
                    for (Map.Entry<String, MessageTempContent> content : tempContents.entrySet()) {
                        MessageInstance instance = MessageInfoFactory.createInstance(content.getValue(),
                                messageSendParam);
                        directMessageInfo.addInstance(content.getKey(), instance);
                    }
                } catch (Exception e) {
                    directMessageInfo.appendError(e.getMessage());
                }

            } else {
                directMessageInfo.appendError("未找到消息模板内容");
            }
        } finally {
            if (latchForContent != null) {
                latchForContent.countDown();
            }
        }
    }

    /**
     * 过滤消息模板
     *
     * @param tempContents 消息模板
     * @param receivers    发送对象
     * @return 过滤后的消息模板
     */
    private BaseResult<Map<String, MessageTempContent>> filter(
            Map<String, MessageTempContent> tempContents, List<MessageReceiver> receivers) {

        if (CollectionUtils.isEmpty(receivers)) {
            return BaseResult.fail("没有解析到发送对象");
        }

        Set<String> keys = new HashSet<>();
        for (MessageReceiver receiver : receivers) {
            String key = receiver.sendGroup();
            if (!tempContents.containsKey(key)) {
                return BaseResult.fail("模板【" + messageSendParam.getTemplateKey() + "】" + key + "未配置内容");
            }
            keys.add(key);
        }

        tempContents.keySet().retainAll(keys);
        return BaseResult.success(tempContents);
    }

    @Override
    public ThreadType threadType() {
        return ThreadType.CONTENT;
    }

}
