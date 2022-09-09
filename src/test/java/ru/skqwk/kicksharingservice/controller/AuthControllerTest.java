package ru.skqwk.kicksharingservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.skqwk.kicksharingservice.dto.AuthOkResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends BaseControllerTest {

  @Test
  public void shouldReturnConflictIfDuplicate() throws Exception {
    String email = generateEmail();

    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRegisterRequest(email, null)))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRegisterRequest(email, null)))
        .andExpect(status().is(HttpStatus.CONFLICT.value()));
  }

  @Test
  public void shouldDeleteAccount() throws Exception {
    String email = generateEmail();
    AuthOkResponse authOkResponse = registerUser(email);

    mockMvc
        .perform(
            delete("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                    HttpHeaders.AUTHORIZATION,
                    config.getTokenPrefix() + authOkResponse.getAuthToken()))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserCredentials(email)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void shouldNotDeleteWithoutToken() throws Exception {

    mockMvc
        .perform(delete("/account").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void shouldNotPassWithWrongPassword() throws Exception {
    String email = generateEmail();

    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRegisterRequest(email, null)))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserCredentials(email, "wrong_password")))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void shouldNotPassWithEmptyRegistration() throws Exception {
    mockMvc
        .perform(post("/register").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
  }
}
