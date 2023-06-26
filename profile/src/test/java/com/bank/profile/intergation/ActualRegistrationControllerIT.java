package com.bank.profile.intergation;

import com.bank.profile.controller.ActualRegistrationController;
import com.bank.profile.entity.ActualRegistrationEntity;
import com.bank.profile.repository.ActualRegistrationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Интеграционные тесты для {@link ActualRegistrationController}
 */
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для ActualRegistrationController")
public class ActualRegistrationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActualRegistrationRepository repository;

    @Autowired
    private ObjectMapper objectMapper;


    private ActualRegistrationEntity entity;


    @BeforeEach
    void createEntityForDB() {
        entity = new ActualRegistrationEntity(
                1L, "hello", "hello", "hello", "hello",
                "hello", "hello", "hello", "hello", "hello", 1L);

        repository.save(entity);

    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по id из БД, позитивный сценарий")
    void readByIdReturnJsonPositiveTest() {
        mockMvc.perform(get("/actual/registration/read/{id}", 1))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(entity))
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение из БД по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeTest() {
        mockMvc.perform(get("/account/details/read/{id}", 0L))
                .andExpectAll(
                        status().isNotFound(),
                        content().string("accountDetailsId с данным id не найден!")
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() {
        ActualRegistrationEntity saveEntity = new ActualRegistrationEntity(2L, "privet", "privet", "privet", "privet",
                "privet", "privet", "privet", "privet", "privet", 2L);

        mockMvc.perform(post("/actual/registration/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveEntity)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(saveEntity))
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Создание, передан null, негативный сценарий")
    void createNullNegativeTest() {
        mockMvc.perform(post("/actual/registration/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpectAll(
                        status().is4xxClientError());
    }


    @Test
    @SneakyThrows
    @DisplayName("Обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        repository.save(new ActualRegistrationEntity(
                2L, "poka", "poka", "poka", "poka",
                "poka", "poka", "poka", "poka",
                "poka", 2L));

        ActualRegistrationEntity updateEntity = new ActualRegistrationEntity(
                2L, "thanks", "thanks", "thanks", "thanks",
                "thanks", "thanks", "thanks", "thanks",
                "thanks", 77L);

        mockMvc.perform(put("/actual/registration/update/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEntity)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(updateEntity))
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        ActualRegistrationEntity updateEntity = new ActualRegistrationEntity(
                0L, "0", "0", "0", "0",
                "0", "0", "0", "0",
                "0", 0L);

        mockMvc.perform(put("/actual/registrations/update/{id}", 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEntity)))
                .andExpectAll(
                        status().isNotFound()
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTestTest() {
        repository.save(new ActualRegistrationEntity(
                2L, "hello", "hello", "hello", "hello",
                "hello", "hello", "hello", "hello", "hello", 2L));

        repository.save(new ActualRegistrationEntity(
                3L, "hello", "hello", "hello", "hello",
                "hello", "hello", "hello", "hello", "hello", 3L));

        mockMvc.perform(get("/actual/registration/read/all")
                        .param("ids", "" + 1L, "" + 2L, "" + 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по нескольким несуществующим id, негативный сценарий")
    void readAllByNonExistIdNegativeTest() {
        mockMvc.perform(get("/actual/registration/read/all")
                        .param("ids", "" + 0, "" + 154, "" + 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(0))
                );
    }
}
