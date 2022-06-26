package com.example.praca.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Szymon Królik
 */
@Service
@AllArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    public boolean finished;
    public EmailService(){};

    /**
     * @param mailMessage
     */
    @Async
    public void sendEmail(SimpleMailMessage mailMessage) {
        javaMailSender.send(mailMessage);
        finished = true;
    }
}

