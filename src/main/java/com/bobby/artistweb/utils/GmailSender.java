package com.bobby.artistweb.utils;

import com.bobby.artistweb.config.CustomAppProperties;
import com.bobby.artistweb.model.ContactMe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

@Component
public class GmailSender {

    private CustomAppProperties customAppProperties;

    private Session newSession = null;
    private Message message;

    private ContactMe contactMe;

    private List<String> emailRecipients = new ArrayList<String>();
    private String sender;
    private String password;

    @Autowired
    public void setCustomAppProperties(CustomAppProperties customAppProperties) {
        this.customAppProperties = customAppProperties;
    }

    public CustomAppProperties getCustomAppProperties() {
        return customAppProperties;
    }

    public ContactMe getContactMe() {
        return contactMe;
    }

    public void setContactMe(ContactMe contactMe) {
        this.contactMe = contactMe;
    }

    private void initProperties() {
        this.sender = this.customAppProperties.getSender();
        this.password = this.customAppProperties.getPassword();
        this.emailRecipients.add(this.customAppProperties.getRecipient());
    }

    private Message draftEmail() throws MessagingException {

        String emailSubject = this.contactMe.getFirstName() + " - A New Message From Artist Web";
        String emailBody = "<div style='text-align:left;'>Hi Golnaz</div>";
        emailBody += "<div style='width:100%; height:80px; line-height:80px;'>There is a new message from artist web.</div>";
        emailBody += "<div style='width: 100%; height:30px; line-height:30px;'>First Name: <span style='color:blue;'>" + this.contactMe.getFirstName() + "</span></div>";
        emailBody += "<div style='width: 100%; height:30px; line-height:30px;'>Email: <span style='color:blue;'>" + this.contactMe.getEmail() + "</span></div>";
        emailBody += "<div style='width: 100%; height:30px; line-height:50px;'>Phone Number: <span style='color:blue'>" + this.contactMe.getPhoneNumber() + "</span></div>";
        emailBody += "<div style='width: 100%; height:30px; line-height:50px;'>Messages: <span style='color:blue'>" + this.contactMe.getMessage() + "</span></div>";

        emailBody += "<div style='margin-top:100px;'>-- Artist Web</div>";

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
        this.initProperties();

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        newSession = Session.getDefaultInstance(properties, null);
    }

    public void sendEmail() throws MessagingException {
        this.setupServerProperties();
        this.draftEmail();

        String emailHost = "smtp.gmail.com";
        Transport transport = newSession.getTransport("smtp");
        transport.connect(emailHost, this.customAppProperties.getSender(), this.customAppProperties.getPassword());
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully");
    }

//    public static void main(String[] args) throws MessagingException {
//        GmailSender gmailSender = new GmailSender();
//        ContactMe contactMe = new ContactMe();
//        contactMe.setFirstName("Yolo");
//        contactMe.setMessage("haha, here is my message body.");
//        gmailSender.setContactMe(contactMe);
//
//
//        gmailSender.sendEmail();
//
//    }
}

