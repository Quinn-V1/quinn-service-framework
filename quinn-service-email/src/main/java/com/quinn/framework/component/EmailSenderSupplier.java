package com.quinn.framework.component;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.message.MessageSender;
import com.quinn.framework.api.message.MessageSenderSupplier;
import com.quinn.framework.api.message.MessageServer;
import com.quinn.framework.model.EmailSender;
import com.quinn.util.constant.enums.MessageTypeEnum;
import com.quinn.util.base.model.BaseResult;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 消息发送器创建工厂
 *
 * @author Qunhua.Liao
 * @since 2020-04-07
 */
public class EmailSenderSupplier implements MessageSenderSupplier {

    /**
     * 连接参数实例
     */
    private static final Map<String, Object> PARAM_MAP = new LinkedHashMap<>();

    static {
        PARAM_MAP.put("mail.smtp.host", "主机名：smtp.126.com");
        PARAM_MAP.put("mail.smtp.port", "端口：25");
        PARAM_MAP.put("mail.smtp.protocol", "协议：smtp");
        PARAM_MAP.put("mail.smtp.account", "帐号：*****@126.com");
        PARAM_MAP.put("mail.smtp.password", "密码：*******");
        PARAM_MAP.put("mail.smtp.from.show.name", "消息中心");

        PARAM_MAP.put("mail.smtp.test.to.account", "测试发送目标");
        PARAM_MAP.put("mail.smtp.test.to.content", "测试发送内容");

        PARAM_MAP.put("mail.smtp.auth", "权限控制：true");
        PARAM_MAP.put("mail.smtp.starttls.required", "是否需要STARTTLS：true");
        PARAM_MAP.put("mail.smtp.starttls.enable", "是否允许STARTTLS：true");
        PARAM_MAP.put("mail.smtp.timeout", "发送超时时间：6000");
    }

    @Override
    public String messageType() {
        return MessageTypeEnum.EMAIL.name();
    }

    @Override
    public BaseResult<MessageSender> create(JSONObject jsonObject) {
        EmailSender emailSender = new EmailSender();
        return emailSender.init(jsonObject);
    }

    @Override
    public BaseResult<Map> connectParamExample() {
        return BaseResult.success(PARAM_MAP);
    }

    @Override
    public BaseResult<MessageSender> create(MessageServer messageServer) {
        EmailSender emailSender = new EmailSender();
        JSONObject connectParam = messageServer.getConnectParam();
        return emailSender.init(connectParam);
    }

}
