package com.bank.profile.intergation.controller;

import com.bank.profile.controller.ProfileController;
import com.bank.profile.entity.ActualRegistrationEntity;
import com.bank.profile.entity.PassportEntity;
import com.bank.profile.entity.ProfileEntity;
import com.bank.profile.entity.RegistrationEntity;
import com.bank.profile.repository.ActualRegistrationRepository;
import com.bank.profile.repository.PassportRepository;
import com.bank.profile.repository.ProfileRepository;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Интеграционные тесты для {@link ProfileController}
 */
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для ProfileController")
public class ProfileControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfileRepository repository;

    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private ActualRegistrationRepository actualRegistrationRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private ProfileEntity entity;

    private final static RegistrationEntity registration = new RegistrationEntity(
            1L, "hello", "hello", "hello", "hello",
            "hello", "hello", "hello", "hello", "hello", 1L);

    private final static ActualRegistrationEntity actualRegistration = new ActualRegistrationEntity(
            1L, "hello", "hello", "hello", "hello",
            "hello", "hello", "hello", "hello", "hello", 1L);

    private final static PassportEntity passport = new PassportEntity(
            1L, 777, 7L, "hello", "hello",
            "hello", "m", LocalDate.now(), "hello", "hello",
            LocalDate.now(), 777, LocalDate.now(), registration);


    @BeforeEach
    void createEntityForDB() {
        entity = new ProfileEntity(1L, 1L, "hello", "hello",
                777L, 777L, passport, actualRegistration);

        passportRepository.save(passport);
        actualRegistrationRepository.save(actualRegistration);
        repository.save(entity);
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по id из БД, позитивный сценарий")
    void readByIdReturnJsonPositiveTest() {
        mockMvc.perform(get("/profile/read/{id}", 1))
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
        mockMvc.perform(get("/profile/read/{id}", 0L))
                .andExpectAll(
                        status().isNotFound(),
                        content().string("profile с данным id не найден!")
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Создание, позитивный сценарий")
    void createPositiveTest() {
        String saveNode = """
                {
                  "id": 2,
                  "phoneNumber": 3464,
                  "email": "string",
                  "nameOnCard": "string",
                  "inn": 763543,
                  "snils": 234546,
                  
                      "passport": {
                        "series": 346446,
                        "number": 2354675,
                        "lastName": "string",
                        "firstName": "string",
                        "middleName": "string",
                        "gender": "m",
                        "birthDate": "2023-06-29",
                        "birthPlace": "string",
                        "issuedBy": "string",
                        "dateOfIssue": "2023-06-29",
                        "divisionCode": 77325,
                        "expirationDate": "2023-06-29",
                        
                        "registration": {
                          "country": "warning",
                          "region": "warning",
                          "city": "warning",
                          "district": "warning",
                          "locality": "warning",
                          "street": "warning",
                          "houseNumber": "warning",
                          "houseBlock": "warning",
                          "flatNumber": "warning",
                          "index": 2
                        }
                  },
                  
                  "actualRegistration": {
                    "country": "Moscow",
                    "index": 5555
                  }
                }
                """;


        mockMvc.perform(post("/profile/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saveNode))
                .andExpectAll(
                        status().isOk(),
                        content().contentType((MediaType.APPLICATION_JSON)),
                        content().json(saveNode)
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Создание, передан null, негативный сценарий")
    void createNullNegativeTest() {
        mockMvc.perform(post("/profile/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpectAll(
                        status().is4xxClientError());
    }


    @Test
    @SneakyThrows
    @DisplayName("Обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        ProfileEntity updateEntity = new ProfileEntity(2L, 2L, "hello_g", "hello_g",
                8887415L, 774528887L, passport, actualRegistration);

        repository.save(new ProfileEntity(2L, 2L, "lo", "lo",
                95252L, 102654L, passport, actualRegistration));


        mockMvc.perform(put("/profile/update/{id}", 2L)
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


        mockMvc.perform(put("/profile/update/{id}", 0L)
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
        repository.save(new ProfileEntity(2L, 2L, "hello", "hello",
                888L, 8888L, passport, actualRegistration));

        repository.save(new ProfileEntity(3L, 3L, "hello", "hello",
                888888L, 88888888L, passport, actualRegistration));


        mockMvc.perform(get("/profile/read/all")
                        .param("ids", "" + 1L, "" + 2L, "" + 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по нескольким несуществующим id, негативный сценарий")
    void readAllByNonExistIdNegativeTest() {
        mockMvc.perform(get("/profile/read/all")
                        .param("ids", "" + 0, "" + 154, "" + 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(0))
                );
    }
}
