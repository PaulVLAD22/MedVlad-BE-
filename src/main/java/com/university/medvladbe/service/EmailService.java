package com.university.medvladbe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendTextEmail(String receiverEmail, String subject, String text){
        String from = "medvladofficial@gmail.com";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(receiverEmail);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
    public void sendHtmlEmail(String receiverEmail, String subject, String htmlText) throws MessagingException {
        String from = "medvladofficial@gmail.com";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject(subject);
        helper.setFrom(from);
        helper.setTo(receiverEmail);

        helper.setText(htmlText, true);

        mailSender.send(message);
    }
}
