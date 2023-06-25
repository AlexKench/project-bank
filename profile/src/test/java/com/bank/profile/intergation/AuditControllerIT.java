package com.bank.profile.intergation;

import com.bank.profile.controller.AuditController;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Интеграционные тесты для {@link AuditController}
 */
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для AuditController")
class AuditControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectNode nodes;

    @BeforeAll
    void createEntityForDB() {
        AuditEntity testEntity1 = new AuditEntity(anyLong(), "one", "one", "one",
                "one", new Timestamp(1L), new Timestamp(1L), "one", "one");

        auditRepository.save(testEntity1);
    }

    @BeforeAll
    void createJson() {
        nodes = objectMapper.createObjectNode();
        nodes.put("entityType", "one");
        nodes.put("operationType", "one");
        nodes.put("createdBy", "one");
        nodes.put("modifiedBy", "one");
        nodes.put("createdAt", "1970-01-01T00:00:00.001+00:00");
        nodes.put("modifiedAt", "1970-01-01T00:00:00.001+00:00");
        nodes.put("newEntityJson", "one");
        nodes.put("entityJson", "one");
    }

    @Test
    @SneakyThrows
    @DisplayName("Чтение по id из БД, позитивный сценарий")
    void readByIdReturnJsonPositiveTest() {
        mockMvc.perform(get("/audit/{id}", 1L))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(nodes.toString()),
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