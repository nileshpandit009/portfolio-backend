package com.homenas.portfoliobackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final String[] to = new String[] {"nileshpandit009@gmail.com"};
    private final String from = "homenas.vrgn@gmail.com";
    private final JavaMailSender sender;

    @Autowired
    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void send(String replyTo, String senderName, String text) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject("New message from " + senderName + " on portfolio website");
        helper.setReplyTo(replyTo);
        helper.setText(text);

        sender.send(message);
    }
}
