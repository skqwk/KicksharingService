package ru.skqwk.kicksharingservice.kafka.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.skqwk.kicksharingservice.kafka.message.MailMessage;
import ru.skqwk.kicksharingservice.service.MailService;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaMailListener {

    private final MailService mailService;

    @KafkaListener(topics = "mail", groupId = "groupId", containerFactory = "factory")
    void listenMailTopic(MailMessage mailMessage) {
        log.info("Need to send message from = {}, to = {}", mailMessage.getFrom(), mailMessage.getTo());
    }


}

