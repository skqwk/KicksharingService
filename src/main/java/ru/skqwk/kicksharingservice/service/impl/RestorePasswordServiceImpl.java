package ru.skqwk.kicksharingservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;
import ru.skqwk.kicksharingservice.service.MailService;
import ru.skqwk.kicksharingservice.service.RestorePasswordService;
import ru.skqwk.kicksharingservice.service.UserService;

import java.io.StringWriter;
import java.util.Properties;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class RestorePasswordServiceImpl implements RestorePasswordService {
  private final UserService userService;
  private final MailService mailService;

  @Override
  public void restorePassword(String email) {
    userService.loadUserByUsername(email);

    String content = generateHtml(UUID.randomUUID().toString());
    try {
      mailService.sendMail(email, "Restore password", content);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String generateHtml(String code) {
    Template template;
    StringWriter writer = new StringWriter();
    VelocityEngine velocityEngine = new VelocityEngine();
    VelocityContext context = new VelocityContext();

    Properties props = new Properties();
    props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    props.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    velocityEngine.init(props);

    try {
      context.put("verificationCode", code);
      template = velocityEngine.getTemplate("templates/email-template.vm");
      template.merge(context, writer);
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
    return writer.toString();
  }
}
