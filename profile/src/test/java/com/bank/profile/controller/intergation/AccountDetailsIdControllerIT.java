package com.bank.profile.controller.intergation;

import com.bank.profile.controller.AccountDetailsIdController;
import com.bank.profile.entity.AccountDetailsIdEntity;
import com.bank.profile.repository.AccountDetailsIdRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты для {@link AccountDetailsIdController}
 */
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для AccountDetailsIdController")
class AccountDetailsIdControllerIT {

    private static final long id = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountDetailsIdRepository repository;

    @Autowired
    private ObjectMapper objectMapper;


    private AccountDetailsIdEntity entity1;
    private AccountDetailsIdEntity entity2;
    private AccountDetailsIdEntity entity3;


    @BeforeAll
    void createEntityForDB() {
        entity1 = new AccountDetailsIdEntity(id, 1L, null);
        entity2 = new AccountDetailsIdEntity(id + 1, 2L, null);
        entity3 = new AccountDetailsIdEntity(id + 2, 3L, null);
        repository.save(entity1);
        repository.save(entity2);
        repository.save(entity3);

    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по id из БД, позитивный сценарий")
    void readByIdReturnJsonPositiveTest() {
        mockMvc.perform(get("/account/details/read/{id}", id))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(entity1))
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
        AccountDetailsIdEntity saveEntity = new AccountDetailsIdEntity(id + 3, 4L, null);
        ObjectNode saveNode = objectMapper.createObjectNode();
        saveNode.put("id", id + 3);
        saveNode.put("accountId", saveEntity.getAccountId());
        saveNode.put("profile", (Short) null);

        mockMvc.perform(post("/account/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveEntity)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(saveNode.toString())
                );
    }

    @Test
    @SneakyThrows
    @DisplayName("Создание, передан null, негативный сценарий")
    void createNullNegativeTest() {
        AccountDetailsIdEntity saveEntity = null;
        mockMvc.perform(post("/account/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveEntity)))
                .andExpectAll(
                        status().is4xxClientError(),
                        content().string("Required request body is missing: " +
                                "public org.springframework.http.ResponseEntity" +
                                "<com.bank.profile.dto.AccountDetailsIdDto> com.bank.profile.controller" +
                                ".AccountDetailsIdController.create(com.bank.profile.dto.AccountDetailsIdDto)")

                );


    }

    @Test
    void update() {
    }

    @Test
    void readAllById() {
    }
}