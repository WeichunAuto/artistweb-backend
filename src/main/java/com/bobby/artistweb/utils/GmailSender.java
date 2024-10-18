package com.bobby.artistweb.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class GmailSender {

    private Session newSession = null;
    private Message message;

    private String firstName;
    private String recipient;
    private String messageBody;
    private List<String> emailRecipients = new ArrayList<String>();

    public GmailSender(String firstName, String messageBody) {
        this.firstName = firstName;
        this.messageBody = messageBody;
    }

    private Message draftEmail() throws MessagingException {

        String emailSubject = this.firstName + " - A New Message From Artist Web";
        String emailBody = "<div style='text-align:left;'>Hi Golnaz</div>";
        emailBody += "<div style='width:100%; height:80px; line-height:80px;'>There is a new message from artist web.</div>";
        emailBody += "<div style='width: 100%; height:30px; line-height:30px;'>First Name: <span style='color:blue;'>Bobby</span></div>";
        emailBody += "<div style='width: 100%; height:30px; line-height:30px;'>Email: <span style='color:blue;'>rachan0210@sina.com</span></div>";
        emailBody += "<div style='width: 100%; height:30px; line-height:50px;'>Phone Number: <span style='color:blue'>02108589588</span></div>";


        emailBody += "<div style='margin-top:100px;'>-- Artist Web</div>";
        this.emailRecipients.add("bobbywangwc@163.com");

        message = new MimeMessage(newSession);
        for (String emailRecipient : this.emailRecipients) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailRecipient));
        }
        message.setSubject(emailSubject);
        message.setText(emailBody);

        MimeMultipart multipart = new MimeMultipart();

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(emailBody, "text/html; charset=utf-8");

        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        return message;
    }

    private void setupServerProperties() {
        Properties properties = System.getProperties();
//        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        newSession = Session.getDefaultInstance(properties, null);
    }

    private void sendEmail() throws MessagingException {
        this.setupServerProperties();
        this.draftEmail();

        String fromUser = "seven.wangweichun@gmail.com";
        String fromUserPassword = "fuuu ddzy rody hzgz";
        String emailHost = "smtp.gmail.com";
        Transport transport = newSession.getTransport("smtp");
        transport.connect(emailHost, fromUser, fromUserPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully");
    }

    public static void main(String[] args) throws MessagingException {
        GmailSender gmailSender = new GmailSender("Bobby", "");
        gmailSender.setupServerProperties();
        gmailSender.draftEmail();
        gmailSender.sendEmail();

    }
}

