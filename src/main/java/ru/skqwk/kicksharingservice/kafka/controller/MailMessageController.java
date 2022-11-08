package ru.skqwk.kicksharingservice.kafka.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skqwk.kicksharingservice.kafka.message.MailMessage;

@RestController
public class MailMessageController {

    private final KafkaTemplate<String, MailMessage> kafkaTemplate;

    public MailMessageController(KafkaTemplate<String, MailMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("message")
    public void publish(@RequestBody MailMessage message) {
        kafkaTemplate.send("mail", message);
    }
}