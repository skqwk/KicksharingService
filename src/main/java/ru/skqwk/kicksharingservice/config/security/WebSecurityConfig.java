package ru.skqwk.kicksharingservice.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.skqwk.kicksharingservice.service.UserService;

import static ru.skqwk.kicksharingservice.enumeration.UserRole.MANAGER;
import static ru.skqwk.kicksharingservice.enumeration.UserRole.USER;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String[] AUTH_WHITELIST = {
    // -- Swagger UI v2
    "/v2/api-docs",
    "/swagger-resources",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security",
    "/swagger-ui.html",
    "/swagger-ui/**",
    "/webjars/**",
  };
  private final UserService userService;
  private final JwtRequestFilter jwtRequestFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final PasswordEncoder passwordEncoder;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // конфигурируем AuthenticationManager, чтобы он знал откуда загружать пользователей
    // для проверки реквизитов
    auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        // не аутентифицировать данные запросы
        .authorizeRequests()
        .antMatchers(AUTH_WHITELIST)
        .permitAll()
        .antMatchers("/auth", "/register", "/ping")
        .permitAll()
        // аутентифицировать данные запросы
        .antMatchers("/user/**", "/profile/history")
        .hasRole(USER.name())
        .antMatchers("/management/**")
        .hasRole(MANAGER.name())
        .antMatchers("/profile/edit", "/profile")
        .hasAnyRole(MANAGER.name(), USER.name())
        .anyRequest()
        .authenticated()
        .and()
        // используем stateless сессию, значит, что сессия не будет хранить состояние пользователя
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // Добавляем фильтр, чтобы валидировать токен на каждый запрос
    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
