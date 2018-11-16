package com.tanker.mail;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author : Tanker
 * @email :zhoukai@zto.cn
 * @date : 2018/11/16
 * @describe : 认证类
 */
public class MyAuthenticator extends Authenticator {

    String userName = null;
    String password = null;

    public MyAuthenticator() {
    }

    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }


}