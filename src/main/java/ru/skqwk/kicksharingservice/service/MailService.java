package ru.skqwk.kicksharingservice.service;

public interface MailService {
  void sendMail(String to, String subject, String content) throws Exception;
}
