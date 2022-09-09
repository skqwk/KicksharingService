package ru.skqwk.kicksharingservice.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.skqwk.kicksharingservice.dto.ModelDTO;
import ru.skqwk.kicksharingservice.dto.NewScooterDTO;
import ru.skqwk.kicksharingservice.dto.RentPointDTO;
import ru.skqwk.kicksharingservice.dto.ScooterWrapper;
import ru.skqwk.kicksharingservice.enumeration.ScooterStatus;
import ru.skqwk.kicksharingservice.message.AuthOkResponse;
import ru.skqwk.kicksharingservice.model.Model;
import ru.skqwk.kicksharingservice.model.RentPoint;
import ru.skqwk.kicksharingservice.model.Scooter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RentPointControllerTest extends EntityControllerTest {

  private AuthOkResponse authOkResponse;

  @BeforeEach
  void setUp() throws Exception {
    authOkResponse = registerManager(generateEmail());
  }

  @Test
  void shouldCreateRentPoint() throws Exception {
    // when: create rentPoint
    RentPointDTO rentPointDTO = RentPointDTO.builder().longitude(0.0).latitude(0.0).build();

    RentPoint rentPoint =
        createRentPointAndExpect(authOkResponse, rentPointDTO, HttpStatus.OK.value());

    // when: then created rentPoint is actual
    Assertions.assertEquals(rentPointDTO.getLatitude(), rentPoint.getLatitude());
    Assertions.assertEquals(rentPointDTO.getLongitude(), rentPoint.getLongitude());

    // when: get rentPoint
    String rentPointString =
        mockMvc
            .perform(
                get("/management/rent-point/{id}", rentPoint.getId())
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then: rentPoint is the same
    rentPoint = mapper.readValue(rentPointString, RentPoint.class);
    Assertions.assertEquals(rentPointDTO.getLatitude(), rentPoint.getLatitude());
    Assertions.assertEquals(rentPointDTO.getLongitude(), rentPoint.getLongitude());
  }

  @Test
  void shouldDeleteRentPointAndSetScootersNotAttached() throws Exception {
    // when: delete rentPoint
    RentPointDTO rentPointDTO = RentPointDTO.builder().longitude(0.0).latitude(0.0).build();

    RentPoint rentPoint =
        createRentPointAndExpect(authOkResponse, rentPointDTO, HttpStatus.OK.value());

    List<Long> scooters = addScootersToRentPoint(rentPoint.getId(), 5);

    Assertions.assertTrue(
        scooters.stream().map(this::getScooter).collect(Collectors.toSet()).stream()
            .allMatch(s -> ScooterStatus.ON_POINT.equals(s.getStatus())));

    mockMvc
        .perform(
            delete("/management/rent-point/{id}", rentPoint.getId())
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
        .andExpect(status().isOk());

    Assertions.assertTrue(
        scooters.stream().map(this::getScooter).collect(Collectors.toSet()).stream()
            .allMatch(s -> ScooterStatus.NOT_ATTACHED.equals(s.getStatus())));

    // then: can't find rentPoint
    mockMvc
        .perform(
            get("/management/rent-point/{id}", rentPoint.getId())
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldUpdateRentPoint() throws Exception {
    // when: create rentPoint
    RentPointDTO rentPointDTO = RentPointDTO.builder().longitude(0.0).latitude(0.0).build();

    RentPoint rentPoint =
        createRentPointAndExpect(authOkResponse, rentPointDTO, HttpStatus.OK.value());

    // then: created rentPoint is actual
    Assertions.assertEquals(rentPointDTO.getLatitude(), rentPoint.getLatitude());
    Assertions.assertEquals(rentPointDTO.getLongitude(), rentPoint.getLongitude());

    // when: update rentPoint
    rentPointDTO.setLatitude(1.0);
    rentPointDTO.setLongitude(2.0);

    String rentPointString =
        mockMvc
            .perform(
                put("/management/rent-point/{id}", rentPoint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(rentPointDTO))
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then: rentPoint is updated
    rentPoint = mapper.readValue(rentPointString, RentPoint.class);
    Assertions.assertEquals(rentPointDTO.getLatitude(), rentPoint.getLatitude());
    Assertions.assertEquals(rentPointDTO.getLongitude(), rentPoint.getLongitude());
  }

  @Test
  void shouldAddAndRemoveScooters() throws Exception {

    // when: create new rentPoint
    RentPointDTO rentPointDTO = RentPointDTO.builder().longitude(1.0).latitude(1.0).build();

    RentPoint rentPoint =
        createRentPointAndExpect(authOkResponse, rentPointDTO, HttpStatus.OK.value());

    // then: rentPoint is empty
    Assertions.assertEquals(0, rentPoint.getScooters().size());

    List<Long> scooters = addScootersToRentPoint(rentPoint.getId(), 10);

    // then: scooters added
    rentPoint = getRentPoint(rentPoint.getId());
    Assertions.assertEquals(10, rentPoint.getScooters().size());

    // when: remove scooters from rentPoint
    scooters.remove(0);
    removeScootersFromRentPoint(rentPoint.getId(), scooters);

    // then: scooters are removed
    rentPoint = getRentPoint(rentPoint.getId());
    Assertions.assertEquals(1, rentPoint.getScooters().size());
  }

  private List<Long> addScootersToRentPoint(Long rentPointId, int amount) throws Exception {
    // when: add scooters to rentPoint
    ModelDTO modelDTO = ModelDTO.builder().name("model-name").manufacturer("manufacturer").build();
    Model model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());

    NewScooterDTO scooterDTO = NewScooterDTO.builder().modelId(model.getId()).build();
    List<Long> scooters = new ArrayList<>();
    for (int i = 0; i < amount; ++i) {
      Long scooterId =
          createScooterAndExpect(authOkResponse, scooterDTO, HttpStatus.OK.value()).getId();
      scooters.add(scooterId);
    }
    ScooterWrapper scooterWrapper = ScooterWrapper.builder().scooters(scooters).build();

    String rentPointString =
        mockMvc
            .perform(
                put("/management/rent-point/{id}/add-scooters", rentPointId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                    .content(mapper.writeValueAsString(scooterWrapper)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return scooters;
  }

  private void removeScootersFromRentPoint(Long rentPointId, List<Long> scootersIds)
      throws Exception {
    String rentPointString =
        mockMvc
            .perform(
                put("/management/rent-point/{id}/remove-scooters", rentPointId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                    .content(
                        mapper.writeValueAsString(
                            ScooterWrapper.builder().scooters(scootersIds).build())))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
  }

  private RentPoint getRentPoint(Long rentPointId) throws Exception {
    String rentPointString =
        mockMvc
            .perform(
                get("/management/rent-point/{id}", rentPointId)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(rentPointString, RentPoint.class);
  }

  @SneakyThrows
  private Scooter getScooter(Long id) {
    String scooterString =
        mockMvc
            .perform(
                get("/management/scooter/{id}", id)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(scooterString, Scooter.class);
  }
}
