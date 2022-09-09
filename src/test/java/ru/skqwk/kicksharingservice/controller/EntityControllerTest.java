package ru.skqwk.kicksharingservice.controller;

import org.springframework.http.MediaType;
import ru.skqwk.kicksharingservice.dto.ModelDTO;
import ru.skqwk.kicksharingservice.dto.NewScooterDTO;
import ru.skqwk.kicksharingservice.dto.RentPointDTO;
import ru.skqwk.kicksharingservice.dto.TariffDTO;
import ru.skqwk.kicksharingservice.message.AuthOkResponse;
import ru.skqwk.kicksharingservice.model.Model;
import ru.skqwk.kicksharingservice.model.RentPoint;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.model.Tariff;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EntityControllerTest extends BaseControllerTest {

  protected Model createModelAndExpect(AuthOkResponse authOkResponse, ModelDTO modelDTO, int status)
      throws Exception {
    String modelString =
        mockMvc
            .perform(
                post("/management/model")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                    .content(mapper.writeValueAsString(modelDTO)))
            .andExpect(status().is(status))
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(modelString, Model.class);
  }

  protected Scooter createScooterAndExpect(
      AuthOkResponse authOkResponse, NewScooterDTO scooterDTO, int status) throws Exception {
    String scooterString =
        mockMvc
            .perform(
                post("/management/scooter")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                    .content(mapper.writeValueAsString(scooterDTO)))
            .andExpect(status().is(status))
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(scooterString, Scooter.class);
  }

  protected Tariff createTariffAndExpect(
      AuthOkResponse authOkResponse, TariffDTO tariffDTO, int status) throws Exception {
    String tariffString =
        mockMvc
            .perform(
                post("/management/tariff")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                    .content(mapper.writeValueAsString(tariffDTO)))
            .andExpect(status().is(status))
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(tariffString, Tariff.class);
  }

  protected RentPoint createRentPointAndExpect(
      AuthOkResponse authOkResponse, RentPointDTO rentPointDTO, int status) throws Exception {
    String rentPointString =
        mockMvc
            .perform(
                post("/management/rent-point")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                    .content(mapper.writeValueAsString(rentPointDTO)))
            .andExpect(status().is(status))
            .andReturn()
            .getResponse()
            .getContentAsString();

    return mapper.readValue(rentPointString, RentPoint.class);
  }
}
