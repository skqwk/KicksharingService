package ru.skqwk.kicksharingservice.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.skqwk.kicksharingservice.dto.ModelDTO;
import ru.skqwk.kicksharingservice.dto.NewScooterDTO;
import ru.skqwk.kicksharingservice.dto.AuthOkResponse;
import ru.skqwk.kicksharingservice.model.Model;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.repo.ScooterRepository;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ScooterControllerTest extends EntityControllerTest {

  private AuthOkResponse authOkResponse;
  @Autowired private ScooterRepository scooterRepository;

  @BeforeEach
  void setUp() throws Exception {
    scooterRepository.deleteAll();
    authOkResponse = registerManager(generateEmail());
  }

  @Test
  void shouldAddNewScooter() throws Exception {
    // when: create new scooter
    ModelDTO modelDTO = ModelDTO.builder().manufacturer("China").name("BMX 2000").build();
    Model model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());
    NewScooterDTO scooterDTO =
        NewScooterDTO.builder().modelId(model.getId()).timeInUse(Duration.ZERO).build();
    Scooter scooter = createScooterAndExpect(authOkResponse, scooterDTO, HttpStatus.OK.value());

    // then: new scooter is created
    Assertions.assertEquals(scooter.getModel().getManufacturer(), model.getManufacturer());
    Assertions.assertEquals(scooter.getModel().getName(), model.getName());
    Assertions.assertEquals(scooter.getTimeInUse(), scooterDTO.getTimeInUse());

    // when: get scooter
    String scooterString =
        mockMvc
            .perform(
                get("/management/scooter/{id}", scooter.getId())
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then: scooter is the same
    scooter = mapper.readValue(scooterString, Scooter.class);
    Assertions.assertEquals(scooter.getModel().getManufacturer(), model.getManufacturer());
    Assertions.assertEquals(scooter.getModel().getName(), model.getName());
    Assertions.assertEquals(scooter.getTimeInUse(), scooterDTO.getTimeInUse());
  }

  @Test
  void shouldDeleteScooter() throws Exception {
    // when: delete scooter
    ModelDTO modelDTO = ModelDTO.builder().manufacturer("China").name("BMX 2000").build();
    Model model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());
    NewScooterDTO scooterDTO =
        NewScooterDTO.builder().modelId(model.getId()).timeInUse(Duration.ZERO).build();
    Scooter scooter = createScooterAndExpect(authOkResponse, scooterDTO, HttpStatus.OK.value());

    mockMvc
        .perform(
            delete("/management/scooter/{id}", scooter.getId())
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
        .andExpect(status().isOk());

    // then: can't find scooter
    mockMvc
        .perform(
            get("/management/scooter/{id}", scooter.getId())
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldUpdateScooter() throws Exception {
    // when: create new scooter
    ModelDTO modelDTO = ModelDTO.builder().manufacturer("China").name("BMX 2000").build();
    Model model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());
    NewScooterDTO scooterDTO =
        NewScooterDTO.builder().modelId(model.getId()).timeInUse(Duration.ZERO).build();
    Scooter scooter = createScooterAndExpect(authOkResponse, scooterDTO, HttpStatus.OK.value());

    // then: new scooter is created
    Assertions.assertEquals(scooter.getModel().getManufacturer(), model.getManufacturer());
    Assertions.assertEquals(scooter.getModel().getName(), model.getName());
    Assertions.assertEquals(scooter.getTimeInUse(), scooterDTO.getTimeInUse());

    // when: update scooter
    modelDTO = ModelDTO.builder().manufacturer("Russia").name("Pioneer").build();
    model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());
    scooterDTO =
        NewScooterDTO.builder().modelId(model.getId()).timeInUse(Duration.ZERO.plusDays(2)).build();
    String scooterString =
        mockMvc
            .perform(
                put("/management/scooter/{id}", scooter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                    .content(mapper.writeValueAsString(scooterDTO)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    scooter = mapper.readValue(scooterString, Scooter.class);

    // then: scooter is updated
    Assertions.assertEquals(scooter.getTimeInUse(), scooterDTO.getTimeInUse());
  }

  @Test
  void shouldFindAllScooters() throws Exception {
    // when: create new scooters
    ModelDTO modelDTO = ModelDTO.builder().manufacturer("China").name("BMX 2000").build();
    Model model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());

    NewScooterDTO scooterDTO1 =
        NewScooterDTO.builder().modelId(model.getId()).timeInUse(Duration.ZERO).build();
    NewScooterDTO scooterDTO2 =
        NewScooterDTO.builder().modelId(model.getId()).timeInUse(Duration.ZERO).build();
    Scooter scooter1 = createScooterAndExpect(authOkResponse, scooterDTO1, HttpStatus.OK.value());
    Scooter scooter2 = createScooterAndExpect(authOkResponse, scooterDTO2, HttpStatus.OK.value());

    String scootersString =
        mockMvc
            .perform(
                get("/management/scooters")
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then: find them all
    Scooter[] scooters = mapper.readValue(scootersString, Scooter[].class);
    Assertions.assertEquals(2, scooters.length);
  }

  @Test
  void simpleUserCantWorkWithScooter() throws Exception {
    ModelDTO modelDTO = ModelDTO.builder().manufacturer("China").name("BMX 2000").build();
    Model model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());

    authOkResponse = registerUser(generateEmail());
    NewScooterDTO scooterDTO =
        NewScooterDTO.builder().modelId(model.getId()).timeInUse(Duration.ZERO).build();
    mockMvc
        .perform(
            post("/management/scooter")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                .content(mapper.writeValueAsString(scooterDTO)))
        .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
  }
}
