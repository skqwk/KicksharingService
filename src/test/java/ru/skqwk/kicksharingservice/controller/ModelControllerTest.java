package ru.skqwk.kicksharingservice.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.skqwk.kicksharingservice.dto.ModelDTO;
import ru.skqwk.kicksharingservice.dto.AuthOkResponse;
import ru.skqwk.kicksharingservice.model.Model;
import ru.skqwk.kicksharingservice.repo.ModelRepository;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ModelControllerTest extends EntityControllerTest {

  private AuthOkResponse authOkResponse;
  @Autowired private ModelRepository modelRepository;

  @BeforeEach
  void setUp() throws Exception {
    authOkResponse = registerManager(generateEmail());
  }

  @Test
  void shouldAddNewModel() throws Exception {
    // when: create new model
    ModelDTO modelDTO = ModelDTO.builder().manufacturer("China").name("BMX 2000").build();
    Model model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());

    // then: new model is created
    Assertions.assertEquals(model.getManufacturer(), modelDTO.getManufacturer());
    Assertions.assertEquals(model.getName(), modelDTO.getName());

    String modelString =
        mockMvc
            .perform(
                get("/management/model/{id}", model.getId())
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then: same model
    model = mapper.readValue(modelString, Model.class);
    Assertions.assertEquals(model.getManufacturer(), modelDTO.getManufacturer());
    Assertions.assertEquals(model.getName(), modelDTO.getName());
  }

  @Test
  void findAllModels() throws Exception {
    int expected = modelRepository.findAll().size();

    ModelDTO modelDTO1 = ModelDTO.builder().manufacturer("China").name("BMX 2000").build();
    ModelDTO modelDTO2 = ModelDTO.builder().manufacturer("Japan").name("Jaguar v89").build();
    createModelAndExpect(authOkResponse, modelDTO1, HttpStatus.OK.value());
    createModelAndExpect(authOkResponse, modelDTO2, HttpStatus.OK.value());

    String modelsString =
        mockMvc
            .perform(
                get("/management/models")
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Model[] models = mapper.readValue(modelsString, Model[].class);
    Assertions.assertEquals(expected + 2, models.length);
  }

  @Test
  void shouldUpdateModel() throws Exception {
    // when: create new model
    ModelDTO modelDTO = ModelDTO.builder().manufacturer("China").name("BMX 2000").build();
    Model model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());

    // then: new model is created
    Assertions.assertEquals(model.getManufacturer(), modelDTO.getManufacturer());
    Assertions.assertEquals(model.getName(), modelDTO.getName());
    // when: update model
    modelDTO = ModelDTO.builder().manufacturer("Russia").name("Pioneer").build();
    String modelString =
        mockMvc
            .perform(
                put("/management/model/{id}", model.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                    .content(mapper.writeValueAsString(modelDTO)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    model = mapper.readValue(modelString, Model.class);

    // then: model is updated
    Assertions.assertEquals(model.getManufacturer(), modelDTO.getManufacturer());
    Assertions.assertEquals(model.getName(), modelDTO.getName());
  }

  @Test
  void shouldDeleteModel() throws Exception {
    // when: delete model
    ModelDTO modelDTO = ModelDTO.builder().manufacturer("China").name("BMX 2000").build();
    Model model = createModelAndExpect(authOkResponse, modelDTO, HttpStatus.OK.value());
    mockMvc
        .perform(
            delete("/management/model/{id}", model.getId())
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
        .andExpect(status().isOk());

    // then: could't find this model
    mockMvc
        .perform(
            get("/management/model/{id}", model.getId())
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken()))
        .andExpect(status().isNotFound());
  }

  @Test
  void simpleUserCantWorkWithModel() throws Exception {
    ModelDTO modelDTO = ModelDTO.builder().manufacturer("China").name("BMX 2000").build();
    authOkResponse = registerUser(generateEmail());
    mockMvc
        .perform(
            post("/management/model")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, config.getTokenPrefix() + authOkResponse.getAuthToken())
                .content(mapper.writeValueAsString(modelDTO)))
        .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
  }
}
