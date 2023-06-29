package com.bank.profile.intergation.controller;

import com.bank.profile.controller.RegistrationController;
import com.bank.profile.entity.ActualRegistrationEntity;
import com.bank.profile.entity.RegistrationEntity;
import com.bank.profile.repository.RegistrationRepository;
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
 * Интеграционные тесты для {@link RegistrationController}
 */
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для RegistrationController")
public class RegistrationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegistrationRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private RegistrationEntity entity;


    @BeforeEach
    void createEntityForDB() {
        entity = new RegistrationEntity(1L, "reg", "reg", "reg", "reg",
                "hello", "reg", "hello", "hello", "reg", 1L);

        repository.save(entity);
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по id из БД, позитивный сценарий")
    void readByIdReturnJsonPositiveTest() {
        mockMvc.perform(get("/registration/read/{id}", 1))
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
        mockMvc.perform(get("/details/read/{id}", 0L))
                .andExpectAll(
                        status().isNotFound()
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() {
        RegistrationEntity saveEntity = new RegistrationEntity(
                repository.findAll().size() + 1L, "hel", "ooo", "ooo", "reg",
                "hello", "reg", "hello", "hello", "reg", 2L);


        mockMvc.perform(post("/registration/create")
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
        mockMvc.perform(post("/registration/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpectAll(
                        status().is4xxClientError());
    }


    @Test
    @SneakyThrows
    @DisplayName("Обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        RegistrationEntity updateEntity = new RegistrationEntity(
                2L, "new reg", "new reg", "new reg", "reg",
                "new reg", "reg", "new reg", "new reg", "reg", 2L);

        repository.save(new RegistrationEntity(
                2L, "hel", "ooo", "ooo", "reg",
                "hello", "reg", "hello", "hello", "reg", 2L));


        mockMvc.perform(put("/registration/update/{id}", 2L)
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


        mockMvc.perform(put("/registrations/update/{id}", 0L)
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
        repository.save(new RegistrationEntity(2L, "hel", "ooo", "ooo", "reg",
                "hello", "reg", "hello", "hello", "reg", 2L));

        repository.save(new RegistrationEntity(3L, "hel", "ooo", "ooo", "reg",
                "hello", "reg", "hello", "hello", "reg", 3L));


        mockMvc.perform(get("/registration/read/all")
                        .param("ids", "" + 1L, "" + 2L, "" + 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по нескольким несуществующим id, негативный сценарий")
    void readAllByNonExistIdNegativeTest() {
        mockMvc.perform(get("/registration/read/all")
                        .param("ids", "" + 0, "" + 154, "" + 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(0))
                );
    }
}
