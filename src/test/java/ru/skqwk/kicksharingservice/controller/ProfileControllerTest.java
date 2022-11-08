package ru.skqwk.kicksharingservice.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skqwk.kicksharingservice.dto.AuthOkResponse;
import ru.skqwk.kicksharingservice.dto.UserAccountDTO;
import ru.skqwk.kicksharingservice.dto.UserAccountEditDTO;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends BaseControllerTest {

  @Test
  void shouldGetProfileForBothUserRole() throws Exception {
    // when: register user
    String email = generateEmail();
    AuthOkResponse authOkResponse = registerUser(email);

    // then: should get actual info
    UserAccountDTO userAccountDTO = getProfile(authOkResponse);
    Assertions.assertEquals(email, userAccountDTO.getEmail());

    // when: register manager
    email = generateEmail();
    authOkResponse = registerManager(email);

    // then: should get actual info for manager too
    userAccountDTO = getProfile(authOkResponse);
    Assertions.assertEquals(email, userAccountDTO.getEmail());
  }

  @Test
  void shouldEditProfileInfo() throws Exception {
    // when: update user account
    String email = generateEmail();
    AuthOkResponse authOkResponse = registerUser(email);

    UserAccountEditDTO userAccountEditDTO =
        UserAccountEditDTO.builder()
            .email("new-email@yandex.ru")
            .password("12345678")
            .age(20)
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/profile/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userAccountEditDTO))
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
        .andExpect(status().isOk());

    // then: should get updated info
    authOkResponse = auth(userAccountEditDTO.getEmail(), userAccountEditDTO.getPassword());

    UserAccountDTO updatedUserAccount = getProfile(authOkResponse);

    Assertions.assertEquals(userAccountEditDTO.getEmail(), updatedUserAccount.getEmail());
    Assertions.assertEquals(userAccountEditDTO.getAge(), updatedUserAccount.getAge());
  }

  @Test
  void getHistoryRent() {}

  private UserAccountDTO getProfile(AuthOkResponse authOkResponse) throws Exception {
    String userAccountString =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/profile")
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(userAccountString, UserAccountDTO.class);
  }
}
