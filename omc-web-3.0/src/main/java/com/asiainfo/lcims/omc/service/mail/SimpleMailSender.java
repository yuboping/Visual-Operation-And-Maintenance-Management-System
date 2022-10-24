package com.asiainfo.lcims.omc.service.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;

public class SimpleMailSender {
    private static final Logger log = LoggerFactory.make();
    /**
     * 发送邮件的props文件
     */
    private transient Properties props = new Properties();
    /**
     * 邮件服务器登录验证
     */
    private transient MailAuthenticator authenticator;

    /**
     * 邮箱session
     */
    private transient Session session;

    /**
     * 初始化邮件发送器
     * 
     * @param smtpHostName
     *            SMTP邮件服务器地址
     * @param username
     *            发送邮件的用户名(地址)
     * @param password
     *            发送邮件的密码
     */
    public SimpleMailSender(final String smtpHostName, final String username,
            final String password) {
        init(username, password, smtpHostName);
    }

    /**
     * 初始化邮件发送器
     * 
     * @param username
     *            发送邮件的用户名(地址)，并以此解析SMTP服务器地址
     * @param password
     *            发送邮件的密码
     */
    public SimpleMailSender(final String username, final String password) {
        // 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
        final String smtpHostName = "smtp." + username.split("@")[1];
        init(username, password, smtpHostName);

    }

    /**
     * 初始化
     * 
     * @param username
     *            发送邮件的用户名(地址)
     * @param password
     *            密码
     * @param smtpHostName
     *            SMTP主机地址
     */
    private void init(String username, String password, String smtpHostName) {
        // 初始化props
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHostName);
        props.put("mail.smtp.connectiontimeout", "3000");
        props.put("mail.smtp.timeout", "3000");
        // 验证
        authenticator = new MailAuthenticator(username, password);
        // 创建session
        session = Session.getInstance(props, authenticator);
    }

    private MimeMessage makeMimeMessage(SimpleMail mail)
            throws AddressException, MessagingException, UnsupportedEncodingException {
        // 邮件内容
        Multipart mp = new MimeMultipart();
        log.info("mail subject:{" + mail.getSubject() + "}");
        // 创建mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        // 设置发信人
        message.setFrom(new InternetAddress(authenticator.getUsername()));
        // 设置主题
        message.setSubject(mail.getSubject());
        // 设置邮件正文内容
        BodyPart bp1 = new MimeBodyPart();
        bp1.setContent(mail.getContent(), "text/html;charset=utf-8");
        mp.addBodyPart(bp1);

        if(!StringUtils.isEmpty(mail.getPartFilePath())){
            BodyPart bp2 = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(mail.getPartFilePath());
            bp2.setDataHandler(new DataHandler(fileds));
            bp2.setFileName(MimeUtility.encodeText(fileds.getName()));
            mp.addBodyPart(bp2);
        }
        
        message.setContent(mp);
        return message;
    }

    /**
     * 群发邮件
     * 
     * @param recipients
     *            收件人们
     * @param mail
     *            邮件对象
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void send(String[] recipients, SimpleMail mail)
            throws AddressException, MessagingException, UnsupportedEncodingException {
        MimeMessage message = makeMimeMessage(mail);
        // 设置收件人
        int num = recipients.length;
        InternetAddress[] addresses = new InternetAddress[num];
        for (int i = 0; i < num; i++) {
            log.info("mail addresses:{" + recipients[i] + "}");
            addresses[i] = new InternetAddress(recipients[i]);
        }
        message.setRecipients(RecipientType.TO, addresses);
        // 发送
        Transport.send(message);
    }

    public static void main(String[] args) throws Exception {
        SimpleMailSender sender = new SimpleMailSender("smtp.139.com",
                "15155123436@139.com",
                "1qaz@WSX");
        
        SimpleMail mail = new SimpleMail();
        mail.setSubject("test_send_mail");
        mail.setContent("巡检报告请查看附件.");
        
        String[] recipients = new String[1];
        recipients[0] = "xinght3@asiainfo-sec.com";
        sender.send(recipients, mail);
    }
}
