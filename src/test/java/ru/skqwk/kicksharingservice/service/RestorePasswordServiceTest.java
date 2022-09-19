package ru.skqwk.kicksharingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ru.skqwk.kicksharingservice.service.impl.MailServiceImpl;
import ru.skqwk.kicksharingservice.service.impl.RestorePasswordServiceImpl;

import java.util.Properties;

class RestorePasswordServiceTest {

    MailServiceImpl mailService;
    UserService userService = Mockito.mock(UserService.class);
    RestorePasswordService restorePasswordService;

    @BeforeEach
    void setUp() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.port", "465");

        javaMailSender.setJavaMailProperties(props);
        javaMailSender.setPassword("y/%-q%,Rz[#SV9J");
        javaMailSender.setUsername("spring-server@yandex.ru");

        mailService = new MailServiceImpl(javaMailSender);
        mailService.setFrom("spring-server@yandex.ru");

        restorePasswordService = new RestorePasswordServiceImpl(userService, mailService);
    }


    @Test
    void restorePassword() {
        String email = "fyrno@yandex.ru";
        Mockito.when(userService.loadUserByUsername(email)).thenReturn(Mockito.any());

        restorePasswordService.restorePassword(email);
    }
}