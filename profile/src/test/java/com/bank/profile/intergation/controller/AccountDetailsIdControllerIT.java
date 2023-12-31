package com.bank.profile.intergation.controller;

import com.bank.profile.controller.AccountDetailsIdController;
import com.bank.profile.entity.RegistrationEntity;
import com.bank.profile.entity.ActualRegistrationEntity;
import com.bank.profile.entity.AccountDetailsIdEntity;
import com.bank.profile.entity.PassportEntity;
import com.bank.profile.entity.ProfileEntity;
import com.bank.profile.repository.AccountDetailsIdRepository;
import com.bank.profile.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Интеграционные тесты для {@link AccountDetailsIdController}
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для AccountDetailsIdController")
class AccountDetailsIdControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountDetailsIdRepository repository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private AccountDetailsIdEntity entity;


    private static final RegistrationEntity registration = new RegistrationEntity(
            1L, "hello", "hello", "hello", "hello",
            "hello", "hello", "hello", "hello", "hello", 1L);

    public static final ActualRegistrationEntity actualRegistration = new ActualRegistrationEntity(
            1L, "hello", "hello", "hello", "hello",
            "hello", "hello", "hello", "hello", "hello", 1L);

    public static final PassportEntity passport = new PassportEntity(
            1L, 777, 7L, "hello", "hello",
            "hello", "m", LocalDate.now(), "hello", "hello",
            LocalDate.now(), 777, LocalDate.now(), registration);

    public static final ProfileEntity profile = new ProfileEntity(
            1L, 1L, "hello", "hello",
            777L, 777L, passport, actualRegistration);


    @BeforeEach
    void createEntityForDB() {
        entity = new AccountDetailsIdEntity(1L, 1L, profile);
        profileRepository.save(profile);
        repository.save(entity);
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по id из БД, позитивный сценарий")
    void readByIdReturnJsonPositiveTest() {
        mockMvc.perform(get("/account/details/read/{id}", 1))
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
        AccountDetailsIdEntity saveEntity = new AccountDetailsIdEntity(repository.findAll().size() + 1L, 222L, profile);
        mockMvc.perform(post("/account/details/create")
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
        mockMvc.perform(post("/account/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpectAll(
                        status().is4xxClientError(),
                        content().string("Required request body is missing: " +
                                "public org.springframework.http.ResponseEntity" +
                                "<com.bank.profile.dto.AccountDetailsIdDto>" +
                                " com.bank.profile.controller.AccountDetailsIdController.create" +
                                "(com.bank.profile.dto.AccountDetailsIdDto)")
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        repository.save(new AccountDetailsIdEntity(2L, 555L, profile));
        AccountDetailsIdEntity updateEntity = new AccountDetailsIdEntity(2L, 78L, profile);
        mockMvc.perform(put("/account/details/update/{id}", 2L)
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
        AccountDetailsIdEntity updateEntity = new AccountDetailsIdEntity(0L, 258L, profile);
        mockMvc.perform(put("/account/details/update/{id}", 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEntity)))
                .andExpectAll(
                        status().isNotFound(),
                        content().string("Обновление невозможно, accountDetailsId не найден!")
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTestTest() {
        repository.save(new AccountDetailsIdEntity(2L, 782L, profile));
        repository.save(new AccountDetailsIdEntity(3L, 953L, profile));
        mockMvc.perform(get("/account/details/read/all")
                        .param("ids", "" + 1L, "" + 2L, "" + 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по нескольким несуществующим id, негативный сценарий")
    void readAllByNonExistIdNegativeTest() {
        mockMvc.perform(get("/account/details/read/all")
                        .param("ids", "" + 0, "" + 154, "" + 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(0))
                );
    }
}