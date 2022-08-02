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

    protected void sendConfirmationToken(String userMail, String token) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String activationLink = "http://146.59.3.226:8080/user/confirm?token=" + token;
        String mailMessage = "Dziękujemy za rejestracje, proszę kliknąć w link aby aktywować konto \n";
        simpleMailMessage.setTo(userMail);
        simpleMailMessage.setSubject("Mail confirmation");
        simpleMailMessage.setFrom("<MAIL>");
        simpleMailMessage.setText(mailMessage + activationLink);
        this.sendEmail(simpleMailMessage);

    }

    protected void sendConfirmationTokenChangedPassword(String userMail, String token) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String confirmLink = "http://146.59.3.226:8080/user/confirm-password?token=" + token;
        String mailMessage = "Hasło do Twojego konta zostało zmienione, proszę potwierdzić zmiany \n";
        simpleMailMessage.setTo(userMail);
        simpleMailMessage.setSubject("Change password confirmation");
        simpleMailMessage.setFrom("<MAIL>");
        simpleMailMessage.setText(mailMessage + confirmLink);
        this.sendEmail(simpleMailMessage);
    }

    protected void sendConfirmationTokenChangedEmail(String userMail, String token) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String confirmLink = "http://146.59.3.226:8080/user/confirm-mail?token=" + token;
        String mailMessage = "Twój email przypisany do konta został zmieniony, proszę potwierdź zmiany \n";
        simpleMailMessage.setTo(userMail);
        simpleMailMessage.setSubject("Change mail confirmation");
        simpleMailMessage.setFrom("<MAIL>");
        simpleMailMessage.setText(mailMessage + confirmLink);
        this.sendEmail(simpleMailMessage);
    }


}

