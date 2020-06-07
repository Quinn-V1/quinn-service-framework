package com.quinn.framework.model;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.message.MessageInstance;
import com.quinn.framework.api.message.MessageSendRecord;
import com.quinn.framework.api.message.MessageSender;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.HttpHeadersConstant;
import com.quinn.util.constant.StringConstant;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件服务
 *
 * @author Qunhua.Liao
 * @since 2020-02-09
 */
@Setter
@Getter
public class EmailSender implements MessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    /**
     * 邮件发送
     */
    private JavaMailSender javaMailSender;

    /**
     * 发件邮箱
     */
    private Address fromAddress;

    /**
     * 测试目标
     */
    private String testTo;

    /**
     * 测试目标
     */
    private String testContent;

    /**
     * 根据参数初始化
     *
     * @param param 参数
     */
    @Override
    public BaseResult init(JSONObject param) {
        if (param == null) {
            return BaseResult.fail("参数为空");
        }

        LOGGER.error(param.toJSONString());

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(param.getString("mail.smtp.host"));
        mailSender.setPort(param.getIntValue("mail.smtp.port"));
        mailSender.setProtocol(param.getString("mail.smtp.protocol"));

        mailSender.setPassword(param.getString("mail.smtp.password"));
        String fromAccount = param.getString("mail.smtp.account");
        mailSender.setUsername(fromAccount);

        Properties javaMailProperties = new Properties();
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);

            javaMailProperties.put("mail.smtp.ssl.enable",
                    BaseConverter.staticConvert(param.get("mail.smtp.ssl.enable"), Boolean.class, false));
            javaMailProperties.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
        }

        javaMailProperties.put("mail.smtp.auth", param.getBoolean("mail.smtp.auth"));
        javaMailProperties.put("mail.smtp.starttls.enable", param.getBoolean("mail.smtp.starttls.enable"));
        javaMailProperties.put("mail.smtp.timeout", param.getIntValue("mail.smtp.timeout"));

        mailSender.setJavaMailProperties(javaMailProperties);
        javaMailSender = mailSender;

        String fromName = param.getString("mail.smtp.from.show.name");
        if (StringUtils.isEmpty(fromName)) {
            fromName = fromAccount;
        }

        try {
            fromName = MimeUtility.encodeText(fromName, "utf-8", null);
            fromAddress = new InternetAddress(fromAccount, fromName);
        } catch (UnsupportedEncodingException e) {
            return BaseResult.fail();
        }

        testTo = param.getString("mail.smtp.test.to.account");
        testContent = param.getString("mail.smtp.test.to.content");
        if (StringUtils.isEmpty(testContent)) {
            testContent = "这是一封测试邮件";
        }

        return BaseResult.success(this);
    }

    /**
     * 创建消息内容
     *
     * @param sendRecord 消息实例
     * @return 消息内容
     */
    @Override
    public BaseResult send(MessageSendRecord sendRecord) {
        MessageInstance messageInfo = sendRecord.getMessageInstance();
        try {
            MimeMessage mimeMessage = createMessage(messageInfo);
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(sendRecord.getReceiverAddress()));
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // FIXME
            e.printStackTrace();
        }

        return BaseResult.SUCCESS;
    }

    @Override
    public BaseResult sendAll(List<MessageSendRecord> sendRecords) {
        Map<MessageInstance, List<MessageSendRecord>> sendRecordMap = CollectionUtil.collectionToListMap(sendRecords,
                messageSendRecord -> messageSendRecord.getMessageInstance());
        for (Map.Entry<MessageInstance, List<MessageSendRecord>> entry : sendRecordMap.entrySet()) {
            MimeMessage mimeMessage = createMessage(entry.getKey());
            List<MessageSendRecord> sendRecordList = entry.getValue();

            try {
                int size = sendRecordList.size();
                Address[] toAdd = new InternetAddress[size];
                for (int i = 0; i < size; i++) {
                    toAdd[i] = new InternetAddress(sendRecordList.get(i).getReceiverAddress());
                }

                mimeMessage.setRecipients(Message.RecipientType.TO, toAdd);
                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                // FIXME
                e.printStackTrace();
            }
        }

        return BaseResult.SUCCESS;
    }

    /**
     * 测试连接服务器
     *
     * @return 测试连接服务器
     */
    @Override
    public BaseResult test() {
        if (StringUtils.isEmpty(testTo)) {
            // FIXME
            return BaseResult.fail("未指定测试目标地址");
        }

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setFrom(fromAddress);
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(testTo));
            String c = MimeUtility.encodeText(testContent, StringConstant.SYSTEM_DEFAULT_CHARSET, null);
            mimeMessage.setSubject(c);
            mimeMessage.setContent(testContent, HttpHeadersConstant.CONTENT_TYPE_HTML);

            javaMailSender.send(mimeMessage);
            return BaseResult.SUCCESS;
        } catch (Exception e) {
            return BaseResult.fail(e.getMessage());
        }
    }

    /**
     * 创建消息内容
     *
     * @param messageInstance 消息实例
     * @return 消息内容
     */
    public MimeMessage createMessage(MessageInstance messageInstance) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.setFrom(fromAddress);
            mimeMessage.setSubject(MimeUtility.encodeText(messageInstance.getSubject(), "utf-8", null));

            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(messageInstance.getContent(), "text/html;charset=UTF-8");
            mp.addBodyPart(mbp);

            String attachments = messageInstance.getAttachment();
            if (!StringUtil.isEmptyInFrame(attachments)) {
                String[] files = attachments.split(";");
                for (int i = 0; i < files.length; i++) {
                    mbp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(files[i]);
                    mbp.setDataHandler(new DataHandler(fds));
                    mbp.setFileName(MimeUtility.encodeText("附件" + (i + 1), "utf-8", null));
                    mp.addBodyPart(mbp);
                }
            }

            mimeMessage.setContent(mp);
            mimeMessage.setSentDate(new Date());
            mimeMessage.saveChanges();

        } catch (MessagingException | UnsupportedEncodingException e) {
            // FIXME
            e.printStackTrace();
        }

        return mimeMessage;
    }

}
