package ru.skqwk.kicksharingservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@AllArgsConstructor
public class JwtTokenUtil {
  private final JwtConfig jwtConfig;
  private final SecretKey secretKey;

  public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .claim("authorities", userDetails.getAuthorities())
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(
            Date.from(Instant.now().plus(jwtConfig.getTokenExpirationAfterDays(), ChronoUnit.DAYS)))
        .signWith(secretKey)
        .compact();
  }

  public String getUsernameFromToken(String token) {
    Jws<Claims> claimsJws =
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    Claims body = claimsJws.getBody();
    return body.getSubject();
  }
}
