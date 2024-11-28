package ru.abarigena.NauJava.Service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender){
        this.mailSender =mailSender;
    }

    /**
     * Отправляет email сообщение.
     *
     * @param to      адрес электронной почты получателя
     * @param subject тема письма
     * @param text    текст письма
     */
    public void sendEmail(String to , String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
