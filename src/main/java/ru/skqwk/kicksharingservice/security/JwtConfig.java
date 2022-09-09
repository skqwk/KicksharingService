package ru.skqwk.kicksharingservice.security;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

/** Класс-конфигурация с данными для генерации Jwt-токена. */
@Getter
@Configuration
public class JwtConfig {
  @Value("${jwt.secretKey}")
  private String secretKey;

  @Value("${jwt.tokenPrefix}")
  private String tokenPrefix;

  @Value("${jwt.tokenExpirationAfterDays}")
  private Integer tokenExpirationAfterDays;

  @Bean
  public SecretKey getSecretKeyForSigning() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }
}
