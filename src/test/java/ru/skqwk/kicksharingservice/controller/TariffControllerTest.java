package ru.skqwk.kicksharingservice.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.skqwk.kicksharingservice.dto.TariffDTO;
import ru.skqwk.kicksharingservice.dto.UserTariffDTO;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.message.AuthOkResponse;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.repo.TariffRepository;

import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TariffControllerTest extends EntityControllerTest {

  private AuthOkResponse authOkResponse;
  @Autowired private TariffRepository tariffRepository;

  @BeforeEach
  void setUp() throws Exception {
    authOkResponse = registerManager(generateEmail());
  }

  @Test
  void shouldAddTariff() throws Exception {
    // when: create tariff
    TariffDTO tariffDTO = createValidSettlementTariffDTO();
    Tariff tariff = createTariffAndExpect(authOkResponse, tariffDTO, HttpStatus.OK.value());

    // then: returned tariff is the same
    Assertions.assertEquals(tariffDTO.getName(), tariff.getName());
    Assertions.assertEquals(tariffDTO.getDescription(), tariff.getDescription());
    Assertions.assertEquals(tariffDTO.getDiscount(), tariff.getDiscount());
    Assertions.assertEquals(tariffDTO.getType(), tariff.getType());
    Assertions.assertEquals(tariffDTO.getSettlementFor(), tariff.getSettlementFor());
    Assertions.assertEquals(tariffDTO.getSettlementCost(), tariff.getSettlementCost());
    Assertions.assertEquals(tariffDTO.getDurationInHours(), tariff.getDurationInHours());

    // when: get tariff
    String tariffString =
        mockMvc
            .perform(
                get("/management/tariff/{id}", tariff.getId())
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then: tariff is the same
    tariff = mapper.readValue(tariffString, Tariff.class);
    Assertions.assertEquals(tariffDTO.getName(), tariff.getName());
    Assertions.assertEquals(tariffDTO.getDescription(), tariff.getDescription());
    Assertions.assertEquals(tariffDTO.getDiscount(), tariff.getDiscount());
    Assertions.assertEquals(tariffDTO.getType(), tariff.getType());
    Assertions.assertEquals(tariffDTO.getSettlementFor(), tariff.getSettlementFor());
    Assertions.assertEquals(tariffDTO.getSettlementCost(), tariff.getSettlementCost());
    Assertions.assertEquals(tariffDTO.getDurationInHours(), tariff.getDurationInHours());
  }

  @Test
  void findTariffsForUser() throws Exception {
    TariffDTO tariffDTO1 = createValidSettlementTariffDTO();
    createTariffAndExpect(authOkResponse, tariffDTO1, HttpStatus.OK.value());
    TariffDTO tariffDTO2 = createValidSettlementTariffDTO();
    createTariffAndExpect(authOkResponse, tariffDTO2, HttpStatus.OK.value());

    authOkResponse = registerUser(generateEmail());

    String userTariffsString =
        mockMvc
            .perform(
                get("/user/tariffs")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Assertions.assertDoesNotThrow(() -> mapper.readValue(userTariffsString, UserTariffDTO[].class));
  }

  @Test
  void findAllTariffs() throws Exception {
    int expected = tariffRepository.findAll().size();

    TariffDTO tariffDTO1 = createValidSettlementTariffDTO();
    createTariffAndExpect(authOkResponse, tariffDTO1, HttpStatus.OK.value());
    TariffDTO tariffDTO2 = createValidSettlementTariffDTO();
    createTariffAndExpect(authOkResponse, tariffDTO2, HttpStatus.OK.value());

    // when: get tariff
    String tariffsString =
        mockMvc
            .perform(
                get("/management/tariffs")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    Tariff[] tariffs = mapper.readValue(tariffsString, Tariff[].class);
    Assertions.assertEquals(expected + 2, tariffs.length);
  }

  @Test
  void shouldDeleteTariff() throws Exception {
    // when: delete tariff
    TariffDTO tariffDTO = createValidSettlementTariffDTO();
    Tariff tariff = createTariffAndExpect(authOkResponse, tariffDTO, HttpStatus.OK.value());

    mockMvc
        .perform(
            delete("/management/tariff/{id}", tariff.getId())
                .header(
                    HttpHeaders.AUTHORIZATION,
                    config.getTokenPrefix() + authOkResponse.getAuthToken()))
        .andExpect(status().isOk());

    // then: can't find tariff
    mockMvc
        .perform(
            get("/management/tariff/{id}", tariff.getId())
                .header(
                    HttpHeaders.AUTHORIZATION,
                    config.getTokenPrefix() + authOkResponse.getAuthToken()))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldUpdateTariff() throws Exception {
    // when: create tariff
    TariffDTO tariffDTO = createValidSettlementTariffDTO();
    Tariff tariff = createTariffAndExpect(authOkResponse, tariffDTO, HttpStatus.OK.value());

    // then: returned tariff is the same
    Assertions.assertEquals(tariffDTO.getName(), tariff.getName());
    Assertions.assertEquals(tariffDTO.getDescription(), tariff.getDescription());
    Assertions.assertEquals(tariffDTO.getDiscount(), tariff.getDiscount());
    Assertions.assertEquals(tariffDTO.getType(), tariff.getType());
    Assertions.assertEquals(tariffDTO.getSettlementFor(), tariff.getSettlementFor());
    Assertions.assertEquals(tariffDTO.getSettlementCost(), tariff.getSettlementCost());
    Assertions.assertEquals(tariffDTO.getDurationInHours(), tariff.getDurationInHours());

    // when: update tariff
    tariffDTO = createValidSubscriptionTariffDTO();
    // when: get tariff
    String tariffString =
        mockMvc
            .perform(
                put("/management/tariff/{id}", tariff.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(tariffDTO))
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then: update is actual
    tariff = mapper.readValue(tariffString, Tariff.class);
    Assertions.assertEquals(tariffDTO.getName(), tariff.getName());
    Assertions.assertEquals(tariffDTO.getDescription(), tariff.getDescription());
    Assertions.assertEquals(tariffDTO.getDiscount(), tariff.getDiscount());
    Assertions.assertEquals(tariffDTO.getType(), tariff.getType());
    Assertions.assertEquals(tariffDTO.getSettlementFor(), tariff.getSettlementFor());
    Assertions.assertEquals(tariffDTO.getSettlementCost(), tariff.getSettlementCost());
    Assertions.assertEquals(tariffDTO.getDurationInHours(), tariff.getDurationInHours());
  }

  @Test
  void simpleUserCantWorkWithTariff() throws Exception {
    authOkResponse = registerUser(generateEmail());
    TariffDTO tariffDTO = createValidSettlementTariffDTO();
    mockMvc
        .perform(
            post("/management/tariff")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                .content(mapper.writeValueAsString(tariffDTO)))
        .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
  }

  private TariffDTO createValidSettlementTariffDTO() {
    return TariffDTO.builder()
        .name("Tariff")
        .description("Description")
        .type(TariffType.SETTLEMENT_TARIFF)
        .settlementFor(ChronoUnit.HOURS)
        .activationCost(30.0)
        .settlementCost(200.0)
        .discount(0.1)
        .build();
  }

  private TariffDTO createValidSubscriptionTariffDTO() {
    return TariffDTO.builder()
        .name("Tariff")
        .description("Description")
        .durationInHours(144)
        .type(TariffType.SUBSCRIPTION_TARIFF)
        .settlementFor(ChronoUnit.HOURS)
        .settlementCost(200.0)
        .tariffCost(100.0)
        .discount(0.1)
        .build();
  }
}
