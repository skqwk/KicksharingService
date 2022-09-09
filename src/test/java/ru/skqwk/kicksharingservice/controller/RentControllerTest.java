package ru.skqwk.kicksharingservice.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skqwk.kicksharingservice.dto.ModelDTO;
import ru.skqwk.kicksharingservice.dto.NewScooterDTO;
import ru.skqwk.kicksharingservice.dto.RentPointDTO;
import ru.skqwk.kicksharingservice.dto.ScooterWrapper;
import ru.skqwk.kicksharingservice.dto.TariffDTO;
import ru.skqwk.kicksharingservice.dto.UserRentDTO;
import ru.skqwk.kicksharingservice.enumeration.RentStatus;
import ru.skqwk.kicksharingservice.enumeration.ScooterStatus;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.dto.AuthOkResponse;
import ru.skqwk.kicksharingservice.model.Model;
import ru.skqwk.kicksharingservice.model.Rent;
import ru.skqwk.kicksharingservice.model.RentPoint;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.model.Tariff;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RentControllerTest extends EntityControllerTest {

  private Scooter scooter;
  private Tariff settlementTariff;
  private Tariff subscriptionTariff;
  private Model model;
  private RentPoint firstRentPoint;
  private RentPoint secondRentPoint;
  private AuthOkResponse authOkResponse;
  private UserRentDTO userRentDTO;
  private String userEmail;
  private String managerEmail;

  @BeforeAll
  void setUp() throws Exception {
    userEmail = generateEmail();
    managerEmail = generateEmail();
    authOkResponse = registerManager(managerEmail);

    // create rentPoint with scooter
    ModelDTO modelDTO = ModelDTO.builder().name("model-name").manufacturer("manufacturer").build();
    this.model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());

    NewScooterDTO scooterDTO = NewScooterDTO.builder().modelId(model.getId()).build();
    this.scooter = createScooterAndExpect(authOkResponse, scooterDTO, HttpStatus.OK.value());

    RentPointDTO rentPointDTO1 = RentPointDTO.builder().longitude(0.0).latitude(0.0).build();
    this.firstRentPoint =
        createRentPointAndExpect(authOkResponse, rentPointDTO1, HttpStatus.OK.value());

    RentPointDTO rentPointDTO2 = RentPointDTO.builder().longitude(10.0).latitude(-10.0).build();
    this.secondRentPoint =
        createRentPointAndExpect(authOkResponse, rentPointDTO2, HttpStatus.OK.value());
    ScooterWrapper scooterWrapper =
        ScooterWrapper.builder().scooters(List.of(scooter.getId())).build();

    mockMvc
        .perform(
            put("/management/rent-point/{id}/add-scooters", firstRentPoint.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                .content(mapper.writeValueAsString(scooterWrapper)))
        .andExpect(status().isOk());

    // create two types of tariff
    TariffDTO settlementTariff =
        TariffDTO.builder()
            .name("Tariff")
            .description("Description")
            .type(TariffType.SETTLEMENT_TARIFF)
            .settlementFor(ChronoUnit.HOURS)
            .activationCost(30.0)
            .settlementCost(200.0)
            .discount(0.1)
            .build();

    TariffDTO subscriptionTariff =
        TariffDTO.builder()
            .name("Tariff")
            .description("Description")
            .durationInHours(144)
            .type(TariffType.SUBSCRIPTION_TARIFF)
            .settlementFor(ChronoUnit.HOURS)
            .settlementCost(200.0)
            .tariffCost(100.0)
            .discount(0.1)
            .build();

    this.settlementTariff =
        createTariffAndExpect(authOkResponse, settlementTariff, HttpStatus.OK.value());
    this.subscriptionTariff =
        createTariffAndExpect(authOkResponse, subscriptionTariff, HttpStatus.OK.value());
  }

  @Test
  @Order(1)
  void startRentWithSettlementTariff() throws Exception {
    authOkResponse = registerUser(userEmail);

    userRentDTO = startRent(firstRentPoint.getId(), scooter.getId(), settlementTariff.getId());

    Assertions.assertEquals(model.getName(), userRentDTO.getScooterModel());
    Assertions.assertEquals(settlementTariff.getName(), userRentDTO.getTariffName());
    Assertions.assertEquals(RentStatus.STARTED, userRentDTO.getStatus());
    Assertions.assertTrue(userRentDTO.getStartedIn().isBefore(Instant.now()));
    Assertions.assertNull(userRentDTO.getFinishedIn());
  }

  @Test
  @Order(2)
  void completeRentWithSettlementTariff() throws Exception {
    userRentDTO = completeRent(secondRentPoint.getId(), userRentDTO.getId());

    Assertions.assertEquals(model.getName(), userRentDTO.getScooterModel());
    Assertions.assertEquals(settlementTariff.getName(), userRentDTO.getTariffName());
    Assertions.assertEquals(RentStatus.FINISHED, userRentDTO.getStatus());
    Assertions.assertTrue(userRentDTO.getStartedIn().isBefore(Instant.now()));
    Assertions.assertTrue(userRentDTO.getStartedIn().isBefore(userRentDTO.getFinishedIn()));
    Assertions.assertTrue(userRentDTO.getStartedIn().isBefore(Instant.now()));
    Assertions.assertNotNull(userRentDTO.getCost());
    Assertions.assertEquals(1, getRentPoint(secondRentPoint.getId()).getScooters().size());
    Assertions.assertEquals(0, getRentPoint(firstRentPoint.getId()).getScooters().size());
    Assertions.assertEquals(ScooterStatus.ON_POINT, getScooter(scooter.getId()).getStatus());
  }

  @Test
  @Order(3)
  void startRentWithSubscriptionTariff() throws Exception {
    authOkResponse = registerUser(generateEmail());

    userRentDTO = startRent(secondRentPoint.getId(), scooter.getId(), subscriptionTariff.getId());

    Assertions.assertEquals(model.getName(), userRentDTO.getScooterModel());
    Assertions.assertEquals(settlementTariff.getName(), userRentDTO.getTariffName());
    Assertions.assertEquals(RentStatus.STARTED, userRentDTO.getStatus());
    Assertions.assertNull(userRentDTO.getFinishedIn());
    Assertions.assertEquals(0, getRentPoint(secondRentPoint.getId()).getScooters().size());
    Assertions.assertEquals(ScooterStatus.IN_RENT, getScooter(scooter.getId()).getStatus());
    Assertions.assertTrue(userRentDTO.getStartedIn().isBefore(Instant.now()));
  }

  @Test
  @Order(4)
  void completeRentWithSubscriptionTariff() throws Exception {
    userRentDTO = completeRent(firstRentPoint.getId(), userRentDTO.getId());

    Assertions.assertEquals(model.getName(), userRentDTO.getScooterModel());
    Assertions.assertEquals(subscriptionTariff.getName(), userRentDTO.getTariffName());
    Assertions.assertEquals(RentStatus.FINISHED, userRentDTO.getStatus());
    Assertions.assertTrue(userRentDTO.getStartedIn().isBefore(Instant.now()));
    Assertions.assertTrue(userRentDTO.getStartedIn().isBefore(userRentDTO.getFinishedIn()));
    Assertions.assertTrue(userRentDTO.getStartedIn().isBefore(Instant.now()));
    Assertions.assertNotNull(userRentDTO.getCost());
    Assertions.assertEquals(1, getRentPoint(firstRentPoint.getId()).getScooters().size());
    Assertions.assertEquals(0, getRentPoint(secondRentPoint.getId()).getScooters().size());
    Assertions.assertEquals(ScooterStatus.ON_POINT, getScooter(scooter.getId()).getStatus());
  }

  @Test
  @Order(5)
  void findPersonalRentHistory() throws Exception {
    String rentString =
        mockMvc
            .perform(
                get("/profile/history")
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    UserRentDTO[] rents = mapper.readValue(rentString, UserRentDTO[].class);
    Assertions.assertEquals(1, rents.length);
  }

  @Test
  @Order(6)
  void findRentHistoryByScooterId() throws Exception {
    AuthOkResponse managerAuthOkResponse = auth(managerEmail, defaultPassword);
    String rentString =
        mockMvc
            .perform(
                get("/management/rents/scooter/{scooterId}", scooter.getId())
                    .header(
                        AUTHORIZATION,
                        config.getTokenPrefix() + managerAuthOkResponse.getAuthToken())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Rent[] rents = mapper.readValue(rentString, Rent[].class);
    Assertions.assertEquals(2, rents.length);
  }

  private UserRentDTO startRent(Long rentPointId, Long scooterId, Long tariffId) throws Exception {
    String rentString =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(
                        "/user/rent/start?rentPointId={rentPointId}&scooterId={scooterId}&tariffId={tariffId}",
                        rentPointId,
                        scooterId,
                        tariffId)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(rentString, UserRentDTO.class);
  }

  private UserRentDTO completeRent(Long rentPointId, Long rentId) throws Exception {
    String rentString =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(
                        "/user/rent/complete?rentPointId={rentPointId}&rentId={rentId}",
                        rentPointId,
                        rentId)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(rentString, UserRentDTO.class);
  }

  private RentPoint getRentPoint(Long id) throws Exception {
    AuthOkResponse managerAuthOkResponse = auth(managerEmail, defaultPassword);
    String rentPointString =
        mockMvc
            .perform(
                get("/management/rent-point/{id}", id)
                    .header(
                        AUTHORIZATION,
                        config.getTokenPrefix() + managerAuthOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(rentPointString, RentPoint.class);
  }

  private Scooter getScooter(Long id) throws Exception {
    AuthOkResponse managerAuthOkResponse = auth(managerEmail, defaultPassword);
    String scooterString =
        mockMvc
            .perform(
                get("/management/scooter/{id}", id)
                    .header(
                        AUTHORIZATION,
                        config.getTokenPrefix() + managerAuthOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(scooterString, Scooter.class);
  }
}
