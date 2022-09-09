package ru.skqwk.kicksharingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skqwk.kicksharingservice.message.AuthOkResponse;
import ru.skqwk.kicksharingservice.message.UserCredentials;
import ru.skqwk.kicksharingservice.message.UserRegisterRequest;
import ru.skqwk.kicksharingservice.security.JwtConfig;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class BaseControllerTest {

  protected final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
  @Autowired protected MockMvc mockMvc;
  @Autowired protected JwtConfig config;
  protected String defaultPassword = "qwerty1234";

  @Value("${manager-token}")
  private String managerToken;

  protected AuthOkResponse registerManager(String email) throws Exception {
    return registerUser(email, managerToken);
  }

  protected AuthOkResponse registerUser(String email) throws Exception {
    return registerUser(email, null);
  }

  private AuthOkResponse registerUser(String email, String managerToken) throws Exception {
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRegisterRequest(email, managerToken)))
        .andExpect(status().isOk());

    return auth(email, defaultPassword);
  }

  protected AuthOkResponse auth(String email, String password) throws Exception {
    String response =
        mockMvc
            .perform(
                post("/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createUserCredentials(email, password)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    AuthOkResponse authOkResponse = mapper.readValue(response, AuthOkResponse.class);
    Assertions.assertFalse(authOkResponse.getAuthToken().isEmpty());
    return authOkResponse;
  }

  String generateEmail() {
    return String.format("%s@yandex.ru", UUID.randomUUID());
  }

  protected String createUserRegisterRequest(String email, String managerToken)
      throws JsonProcessingException {
    return createUserRegisterRequest(email, defaultPassword, 19, managerToken);
  }

  protected String createUserRegisterRequest(
      String email, String password, int age, String managerToken) throws JsonProcessingException {
    return mapper.writeValueAsString(
        UserRegisterRequest.builder()
            .managerToken(managerToken)
            .email(email)
            .password(password)
            .age(age)
            .build());
  }

  protected String createUserCredentials(String email) throws JsonProcessingException {
    return createUserCredentials(email, defaultPassword);
  }

  protected String createUserCredentials(String email, String password)
      throws JsonProcessingException {
    return mapper.writeValueAsString(
        UserCredentials.builder().email(email).password(password).build());
  }
}
