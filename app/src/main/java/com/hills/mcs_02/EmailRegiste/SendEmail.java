package com.hills.mcs_02.EmailRegiste;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

public class SendEmail {
    /** Mail delivery protocol */
    private String PROTOCOL = "smtp";
    /** SMTP mail server */
    private String HOST = "smtp.163.com";
    /** SMTP mail server default port ：25 */
    private String PORT = "25";
    /** Whether authentication is required */
    private String IS_AUTH = "true" ;
    /** Whether to enable debug mode */
    private String IS_ENABLED_DEBUG_MOD = "true";
    private String emailSender;
    private String emailRecipient;
    /** Initialize the session information connected to the mail server */
    private Properties props;
    private String mUsername;
    private String mPassword;

    /**
     * SMTP and 163 mailbox format are used by default, and port 25 is used.
     * Note that password is not an email password but an authorization code.
     * The default sender is the same as the user name, which can be reset if needed by calling the setEmail_sender method.
     * */
    public SendEmail(String pEmailRecipient,String pUsername,String pPassword){
        emailSender = pUsername;
        emailRecipient = pEmailRecipient;
        mUsername = pUsername;
        mPassword = pPassword;
        props = new Properties();
        props.setProperty("mail.transport.protocol", PROTOCOL);
        props.setProperty("mail.smtp.host", HOST);
        props.setProperty("mail.smtp.port", PORT);
        props.setProperty("mail.smtp.auth", IS_AUTH);
        props.setProperty("mail.debug", IS_ENABLED_DEBUG_MOD);
    }

    public void sendTextEmail(String subject,String content) throws Exception {
        Session session = Session.getDefaultInstance(props);

        MimeMessage message = new MimeMessage(session);
       /** Set the sender */
        message.setFrom(new InternetAddress(emailSender));
        /** Set the subject of the message */
        message.setSubject(subject);
        /** Set the recipient */
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailRecipient));
        /** Set the time to send */
        message.setSentDate(new Date());
        /** Set the plain text content to the message body */
        message.setText(content);
       /** Save and generate the final message content */
        message.saveChanges();

        Transport transport = session.getTransport();
        /**  Open the connection */
        transport.connect(mUsername, mPassword);
        /**  The Message object is passed to the Transport object to send the message */
        transport.sendMessage(message, message.getAllRecipients());
        /** Close the connection */
        transport.close();
    }

}