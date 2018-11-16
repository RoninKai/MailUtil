package com.tanker.mail;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * @author : Tanker
 * @email :zhoukai@zto.cn
 * @date : 2018/11/16
 * @describe : 发送邮件工具类
 */
public class SendMailUtil {
    private static final String HOST = "smtp.163.com";
    private static final String PORT = "465";
    private static final String FROM_ADD = "zhoukai940510@163.com";
    private static final String FROM_PSW = "qwe123";

    public static void send(String toAdd,String mailContont) {
        final MailInfo mailInfo = creatMail(toAdd,mailContont);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMail(mailInfo);
            }
        }).start();
    }

    public static void send(final File file, String toAdd,String mailContont) {
        final MailInfo mailInfo = creatMail(toAdd,mailContont);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendFileMail(mailInfo, file);
            }
        }).start();
    }

    @NonNull
    private static MailInfo creatMail(String toAdd,String logContent) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD);
        mailInfo.setPassword(FROM_PSW);
        mailInfo.setFromAddress(FROM_ADD);
        mailInfo.setToAddress(toAdd);
        mailInfo.setSubject("App Crash Log");
        mailInfo.setContent(logContent);
        return mailInfo;
    }

}