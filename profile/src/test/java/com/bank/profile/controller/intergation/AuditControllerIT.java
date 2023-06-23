package com.bank.profile.controller.intergation;

import com.bank.profile.entity.AuditEntity;
import com.bank.profile.repository.AuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class AuditControllerIT {

    private static final long id = 1L;

    private ObjectNode json1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void createEntityForDB() {
        AuditEntity testEntity1 = new AuditEntity(id, "one", "one", "one",
                "one", new Timestamp(1L), new Timestamp(1L), "one", "one");

        auditRepository.save(testEntity1);
    }

    @BeforeEach
    void createJson() {
        json1 = objectMapper.createObjectNode();
        json1.put("id", id);
        json1.put("entityType", "one");
        json1.put("operationType", "one");
        json1.put("createdBy", "one");
        json1.put("modifiedBy", "one");
        json1.put("createdAt", "1970-01-01T00:00:00.001+00:00");
        json1.put("modifiedAt", "1970-01-01T00:00:00.001+00:00");
        json1.put("newEntityJson", "one");
        json1.put("entityJson", "one");
    }

    @Test
    @SneakyThrows
    @DisplayName("Чтение по id из БД, позитивный сценарий")
    void readByIdReturnJsonPositiveTest() {
        mockMvc.perform(get("/audit/{id}", id))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(json1.toString())
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