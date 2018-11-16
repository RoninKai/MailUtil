package com.tanker.mail;


import android.util.Log;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * @author : Tanker
 * @email :zhoukai@zto.cn
 * @date : 2018/11/16
 * @describe : 邮件发送器
 */
public class MailSender {
    /**
     * 以文本格式发送邮件
     *
     * @param mailInfo 待发送的邮件的信息
     */
    public boolean sendTextMail(final MailInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }

        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            Message mailMessage = new MimeMessage(sendMailSession);
            Address from = new InternetAddress(mailInfo.getFromAddress());
            mailMessage.setFrom(from);
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());
            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo
     * @return
     */
    public static boolean sendHtmlMail(MailInfo mailInfo) {

        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();

        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            Message mailMessage = new MimeMessage(sendMailSession);
            Address from = new InternetAddress(mailInfo.getFromAddress());
            mailMessage.setFrom(from);
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            mailMessage.setContent(mainPart);
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 发送带附件的邮件
     *
     * @param info
     * @param file
     * @return
     */
    public boolean sendFileMail(MailInfo info, File file) {
        Message attachmentMail = createAttachmentMail(info, file);
        try {
            Transport.send(attachmentMail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建带有附件的邮件
     *
     * @param info
     * @param file
     * @return
     */
    private Message createAttachmentMail(final MailInfo info, File file) {
        MimeMessage message = null;
        Properties pro = info.getProperties();
        try {
            Session sendMailSession = Session.getInstance(pro, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(info.getUserName(), info.getPassword());
                }
            });
            message = new MimeMessage(sendMailSession);
            Address from = new InternetAddress(info.getFromAddress());
            message.setFrom(from);
            Address to = new InternetAddress(info.getToAddress());
            message.setRecipient(Message.RecipientType.TO, to);
            message.setSubject(info.getSubject());
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(info.getContent(), "text/html;charset=UTF-8");
            MimeMultipart mp = new MimeMultipart();
            mp.addBodyPart(text);
            MimeBodyPart attach = new MimeBodyPart();
            FileDataSource ds = new FileDataSource(file);
            DataHandler dh = new DataHandler(ds);
            attach.setDataHandler(dh);
            attach.setFileName(MimeUtility.encodeText(dh.getName()));
            mp.addBodyPart(attach);
            mp.setSubType("mixed");
            message.setContent(mp);
            message.saveChanges();
        } catch (Exception e) {
            Log.e("TAG", "创建带附件的邮件失败");
            e.printStackTrace();
        }
        return message;
    }

}