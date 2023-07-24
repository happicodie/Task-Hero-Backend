package com.fivesigma.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author Andy
 * @date 2022/11/16
 */
@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender mailSender;

    public void send(String to_email, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("andy.w.1091025911@gmail.com");
        message.setTo(to_email);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
    }
}
