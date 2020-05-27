package com.quinn.framework.model;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.message.MessageInfo;
import com.quinn.framework.api.message.MessageSender;
import com.quinn.util.base.model.BaseResult;
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
            javaMailProperties.put("mail.smtp.ssl.enable", "true");
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
     * @param messageInfo 消息实例
     * @return 消息内容
     */
    @Override
    public BaseResult send(MessageInfo messageInfo) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.setFrom(fromAddress);
            mimeMessage.setSubject(MimeUtility.encodeText(messageInfo.getMessageSubject(), "utf-8", null));

            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(messageInfo.getMessageContent(), "text/html;charset=UTF-8");
            mp.addBodyPart(mbp);

            String attachments = messageInfo.getMessageAttachment();
            if (!StringUtils.isEmpty(attachments)) {
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
            return BaseResult.fail("未指定测试目标地址");
        }

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setFrom(fromAddress);
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(testTo));
            String c = MimeUtility.encodeText(testContent, "utf-8", null);
            mimeMessage.setSubject(c);
            mimeMessage.setContent(testContent, "text/html;charset=UTF-8");

            javaMailSender.send(mimeMessage);
            return BaseResult.SUCCESS;
        } catch (Exception e) {
            return BaseResult.fail(e.getMessage());
        }
    }
}
