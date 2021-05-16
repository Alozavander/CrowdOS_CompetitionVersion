package com.hills.mcs_02.EmailRegiste;

/*
 * Created by LJH on 2020/3/17
 * 邮件发送类
 */

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    // 邮件发送协议，有"smtp"
    private String PROTOCOL = "smtp";
    // SMTP邮件服务器，对应协议，比如"smtp.163.com"
    private String HOST = "smtp.163.com";
    // SMTP邮件服务器默认端口为"25"
    private String PORT = "25";
    // 是否要求身份认证 "true"
    private String IS_AUTH = "true" ;
    // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
    private String IS_ENABLED_DEBUG_MOD = "true";
    // 发件人
    private String email_sender;
    // 收件人
    private String email_recipient;
    // 初始化连接邮件服务器的会话信息
    private Properties props;

    //"zeronandroidtest@163.com" 测试用
    private String mUsername;

    //"UGQEGXIPKFFLNHEQ" 测试用
    private String mPassword;


    //默认使用smtp和163邮箱格式，端口使用25，注意password不是邮箱密码而是授权码。默认发件人和用户名一样，如若需要，可调用setEmail_sender方法重置。
    public SendEmail(String pEmail_recipient,String pUsername,String pPassword){
        email_sender = pUsername;
        email_recipient = pEmail_recipient;
        mUsername = pUsername;
        mPassword = pPassword;
        props = new Properties();
        props.setProperty("mail.transport.protocol", PROTOCOL);
        props.setProperty("mail.smtp.host", HOST);
        props.setProperty("mail.smtp.port", PORT);
        props.setProperty("mail.smtp.auth", IS_AUTH);
        props.setProperty("mail.debug", IS_ENABLED_DEBUG_MOD);
    }

    //可自定义协议和对应服务器以及端口
    public SendEmail(String pEmail_recipient,String pUsername,String pPassword,String pPROTOCOL,String pHOST,String pPORT){
        email_sender = pUsername;
        email_recipient = pEmail_recipient;
        mUsername = pUsername;
        mPassword = pPassword;
        PROTOCOL = pPROTOCOL;
        HOST = pHOST;
        PORT = pPORT;
        props = new Properties();
        props.setProperty("mail.transport.protocol", PROTOCOL);
        props.setProperty("mail.smtp.host", HOST);
        props.setProperty("mail.smtp.port", PORT);
        props.setProperty("mail.smtp.auth", IS_AUTH);
        props.setProperty("mail.debug", IS_ENABLED_DEBUG_MOD);
    }

    public void setEmail_sender(String pEmail_sender) {
        email_sender = pEmail_sender;
    }

    /*
     * 发送简单的文本验证码邮件，Code为验证码
     * @param subject 邮件主题
     * @param content 内容
     * @throws Exception
     */
    public void sendTextEmail(String subject,String content) throws Exception {
        // 创建Session实例对象
        Session session = Session.getDefaultInstance(props);

        // 创建MimeMessage实例对象
        MimeMessage message = new MimeMessage(session);
        // 设置发件人
        message.setFrom(new InternetAddress(email_sender));
        // 设置邮件主题
        message.setSubject(subject);
        // 设置收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email_recipient));
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置纯文本内容为邮件正文
        message.setText(content);
        // 保存并生成最终的邮件内容
        message.saveChanges();

        // 获得Transport实例对象
        Transport transport = session.getTransport();
        // 打开连接
        transport.connect(mUsername, mPassword);
        // 将message对象传递给transport对象，将邮件发送出去
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();
    }

    /*
     * 发送简单的html验证码邮件，Code为验证码
     * @param subject 邮件主题
     * @param content 内容
     * @param Code
     * @throws Exception
     */
    public void sendHtmlEmail(String subject,String content,long code) throws Exception {
        // 创建Session实例对象
        Session session = Session.getInstance(props, new MyAuthenticator(mUsername,mPassword));

        // 创建MimeMessage实例对象
        MimeMessage message = new MimeMessage(session);
        // 设置邮件主题
        message.setSubject(subject);
        // 设置发送人
        message.setFrom(new InternetAddress(email_sender));
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置收件人
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email_recipient));
        // 设置html内容为邮件正文，指定MIME类型为text/html类型，并指定字符编码为gbk
        message.setContent("<span style='color:blue;'>" + content + "</span>"+"<span style='color:red;'>"+ code+"</span>",
                "text/html;charset=gbk");
        // 保存并生成最终的邮件内容
        message.saveChanges();
        // 发送邮件
        Transport.send(message);
    }


    /**
     * 向邮件服务器提交认证信息
     */
    static class MyAuthenticator extends Authenticator {

        private String username = "";

        private String password = "";


        public MyAuthenticator(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication(username, password);
        }
    }

}