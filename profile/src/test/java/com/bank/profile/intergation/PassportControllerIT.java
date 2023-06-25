package com.bank.profile.intergation;

import com.bank.profile.controller.PassportController;
import com.bank.profile.entity.PassportEntity;
import com.bank.profile.entity.RegistrationEntity;
import com.bank.profile.repository.PassportRepository;
import com.bank.profile.repository.RegistrationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты для {@link PassportController}
 */
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для PassportController")
public class PassportControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassportRepository repository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private PassportEntity entity;

    private RegistrationEntity registration;

    private RegistrationEntity registration2;


    @BeforeEach
    void createEntityForDB() {
        registration = new RegistrationEntity(
                1L, "registration", "registration", "registration", "registration",
                "registration", "registration", "registration", "registration",
                "registration", 1L);

        registration2 = new RegistrationEntity(
                2L, "registration", "registration", "registration", "registration",
                "registration", "registration", "registration", "registration",
                "registration", 2L);

        registrationRepository.save(registration);

        entity = new PassportEntity(
                1L, 111, 1L, "hello", "hello",
                "hello", "m", LocalDate.now(), "hello", "hello",
                LocalDate.now(), 1, LocalDate.now(), registration);

        repository.save(entity);

    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по id из БД, позитивный сценарий")
    void readByIdReturnJsonPositiveTest() {
        mockMvc.perform(get("/passport/read/{id}", 1))
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
        mockMvc.perform(get("/passport/read/{id}", 0L))
                .andExpectAll(
                        status().isNotFound(),
                        content().string("passport с данным id не найден!")
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() {
        PassportEntity saveEntity = new PassportEntity(
                2L, 222, 2L, "hello", "hello",
                "hello", "m", LocalDate.now(), "hello", "hello",
                LocalDate.now(), 2, LocalDate.now(), registration2);


        mockMvc.perform(post("/passport/create")
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
        mockMvc.perform(post("/passport/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpectAll(
                        status().is4xxClientError());
    }


    @Test
    @SneakyThrows
    @DisplayName("Обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        repository.save(new PassportEntity(
                2L, 555, 5L, "privet", "privet",
                "privet", "w", LocalDate.now(), "privet", "privet",
                LocalDate.now(), 5, LocalDate.now(), registration));

        PassportEntity updateEntity = new PassportEntity(
                2L, 555, 5L, "im", "im",
                "im", "m", LocalDate.now(), "im", "im",
                LocalDate.now(), 5, LocalDate.now(), registration);

        mockMvc.perform(put("/passport/update/{id}", 2L)
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
        PassportEntity updateEntity = new PassportEntity(
                0L, 0, 0L, "0", "0",
                "0", "m", LocalDate.now(), "0", "0",
                LocalDate.now(), 0, LocalDate.now(), registration);

        mockMvc.perform(put("/passport/update/{id}", 0L)
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
        repository.save(new PassportEntity(2L
                , 222, 2L, "bye", "bye",
                "hello", "m", LocalDate.now(), "bye", "bye",
                LocalDate.now(), 2, LocalDate.now(), registration));

        repository.save(new PassportEntity(
                3L, 333, 3L, "hi", "hi",
                "hi", "m", LocalDate.now(), "hi", "hi",
                LocalDate.now(), 3, LocalDate.now(), registration));

        mockMvc.perform(get("/passport/read/all")
                        .param("ids", "" + 1L, "" + 2L, "" + 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по нескольким несуществующим id, негативный сценарий")
    void readAllByNonExistIdNegativeTest() {
        mockMvc.perform(get("/passport/read/all")
                        .param("ids", "" + 0, "" + 154, "" + 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(0))
                );
    }
}
