package ru.skqwk.kicksharingservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.skqwk.kicksharingservice.service.MailService;

import javax.mail.internet.MimeMessage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Setter
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String from;

  @Override
  public void sendMail(String to, String subject, String content) throws Exception {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
    try {
      helper.setText(content, true);
      helper.setTo(to);
      helper.setFrom(from);
      helper.setSubject(subject);
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
    log.info("Send message");
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> mailSender.send(mimeMessage));
    future.get();
    log.info("Message is successfully sent to {}", to);
  }
}
