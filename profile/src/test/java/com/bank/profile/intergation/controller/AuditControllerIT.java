package com.bank.profile.intergation.controller;

import com.bank.profile.controller.AuditController;
import com.bank.profile.entity.AuditEntity;
import com.bank.profile.repository.AuditRepository;
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

import java.sql.Timestamp;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


/**
 * Интеграционные тесты для {@link AuditController}
 */
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для AuditController")
class AuditControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private AuditEntity entity;


    @BeforeEach
    void createEntityForDB() {
        entity = new AuditEntity(1L, "one", "one", "one",
                "one", new Timestamp(1L), new Timestamp(1L), "one", "one");

        auditRepository.save(entity);
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение по id из БД, позитивный сценарий")
    void readByIdReturnJsonPositiveTest() {
        mockMvc.perform(get("/audit/{id}", 1L))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(entity)),
                        jsonPath("$.id").exists()
                );
    }


    @Test
    @SneakyThrows
    @DisplayName("Чтение из БД по несуществующему id, негативный сценарий")
    void readByNonExistIdNegativeTest() {
        mockMvc.perform(get("/audit/{id}", 0L))
                .andExpectAll(
                        status().isNotFound(),
                        content().string("Не найден аудит с ID  " + 0L)
                );
    }

}