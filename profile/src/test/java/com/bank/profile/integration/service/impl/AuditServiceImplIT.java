package com.bank.profile.integration.service.impl;

import com.bank.profile.dto.AuditDto;
import com.bank.profile.entity.AuditEntity;
import com.bank.profile.repository.AuditRepository;
import com.bank.profile.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для ActualRegistrationServiceImp")
class AuditServiceImplIT {

    @Autowired
    private AuditService service;

    @Autowired
    private AuditRepository repository;

    private AuditEntity entity;


    @BeforeEach
    void createEntityForDB() {
        entity = new AuditEntity(1L, "one", "one", "one", "one",
                new Timestamp(55L), new Timestamp(55L), "one", "one");
        repository.save(entity);
    }


    @Test
    @DisplayName("Поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditDto result = service.findById(entity.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(entity.getId(), result.getId()),
                () -> assertEquals(entity.getEntityType(), result.getEntityType()),
                () -> assertEquals(entity.getOperationType(), result.getOperationType()),
                () -> assertEquals(entity.getCreatedBy(), result.getCreatedBy()),
                () -> assertEquals(entity.getModifiedBy(), result.getModifiedBy()),
                () -> assertEquals(entity.getCreatedAt(), result.getCreatedAt()),
                () -> assertEquals(entity.getModifiedAt(), result.getModifiedAt()),
                () -> assertEquals(entity.getNewEntityJson(), result.getNewEntityJson()),
                () -> assertEquals(entity.getEntityJson(), result.getEntityJson())
        );
    }


    @Test
    @DisplayName("Поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        assertEquals("Не найден аудит с ID  " + -54,
                assertThrows(EntityNotFoundException.class,
                        () -> service.findById(-54L)).getMessage());
    }

}