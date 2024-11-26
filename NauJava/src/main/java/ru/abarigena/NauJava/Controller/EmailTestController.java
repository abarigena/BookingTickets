package ru.abarigena.NauJava.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.abarigena.NauJava.Service.EmailService.EmailService;

@RestController
public class EmailTestController {
    private final EmailService emailService;

    @Autowired
    public EmailTestController(EmailService emailService){
        this.emailService = emailService;
    }

    @GetMapping("/test-email")
    public String sendTestEmail(){
        try {
            emailService.sendEmail("danil.galanov2017@gmail.com", "Тестовое письмо", "Это тестовое сообщение SMTP.");
            return "Письмо успешно отправлено!";
        } catch (Exception e) {
            return "Ошибка отправки: " + e.getMessage();
        }
    }
}
