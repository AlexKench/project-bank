package com.bank.profile.intergation.service.impl;

import com.bank.profile.dto.ActualRegistrationDto;
import com.bank.profile.entity.ActualRegistrationEntity;
import com.bank.profile.repository.ActualRegistrationRepository;
import com.bank.profile.service.impl.ActualRegistrationServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Интеграционные тесты для {@link ActualRegistrationServiceImp}
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для ActualRegistrationServiceImp")
class ActualRegistrationServiceImpIT {

    @Autowired
    private ActualRegistrationServiceImp service;

    @Autowired
    private ActualRegistrationRepository repository;


    private ActualRegistrationEntity entity;


    @BeforeEach
    void createEntityForDB() {
        entity = new ActualRegistrationEntity(
                repository.findAll().size() + 1L, "one", "one", "one", "one",
                "one", "one", "one", "one", "one", 1L);

        repository.save(entity);
    }


    @Test
    @DisplayName("Поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        ActualRegistrationDto result = service.findById(entity.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(entity.getId(), result.getId()),
                () -> assertEquals(entity.getCountry(), result.getCountry()),
                () -> assertEquals(entity.getRegion(), result.getRegion()),
                () -> assertEquals(entity.getCity(), result.getCity()),
                () -> assertEquals(entity.getDistrict(), result.getDistrict()),
                () -> assertEquals(entity.getLocality(), result.getLocality()),
                () -> assertEquals(entity.getStreet(), result.getStreet()),
                () -> assertEquals(entity.getHouseNumber(), result.getHouseNumber()),
                () -> assertEquals(entity.getHouseBlock(), result.getHouseBlock()),
                () -> assertEquals(entity.getFlatNumber(), result.getFlatNumber()),
                () -> assertEquals(entity.getIndex(), result.getIndex())
        );
    }


    @Test
    @DisplayName("Поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        assertEquals("actualRegistration с данным id не найден!",
                assertThrows(EntityNotFoundException.class,
                        () -> service.findById(-54L)).getMessage());
    }


    @Test
    @DisplayName("Сохранение по id, позитивный сценарий")
    void saveByIdPositiveTest() {
        ActualRegistrationDto saveDto = new ActualRegistrationDto(
                repository.findAll().size() + 1L, "two", "two", "two", "two",
                "two", "two", "two", "two", "two", 2L);

        ActualRegistrationDto result = service.save(saveDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(saveDto.getId(), result.getId()),
                () -> assertEquals(saveDto.getCountry(), result.getCountry()),
                () -> assertEquals(saveDto.getRegion(), result.getRegion()),
                () -> assertEquals(saveDto.getCity(), result.getCity()),
                () -> assertEquals(saveDto.getDistrict(), result.getDistrict()),
                () -> assertEquals(saveDto.getLocality(), result.getLocality()),
                () -> assertEquals(saveDto.getStreet(), result.getStreet()),
                () -> assertEquals(saveDto.getHouseNumber(), result.getHouseNumber()),
                () -> assertEquals(saveDto.getHouseBlock(), result.getHouseBlock()),
                () -> assertEquals(saveDto.getFlatNumber(), result.getFlatNumber()),
                () -> assertEquals(saveDto.getIndex(), result.getIndex())
        );
    }


    @Test
    @DisplayName("Сохранение обьекта с пустыми полями, негативный сценарий")
    void saveEmptyNegativeTest() {
        assertThrows(DataIntegrityViolationException.class
                , () -> service.save(new ActualRegistrationDto()));
    }


    @Test
    @DisplayName("Обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        ActualRegistrationDto saveDto = new ActualRegistrationDto(
                1L, "update", "update", "update", "update",
                "update", "update", "update", "update", "update", 15L
        );

        ActualRegistrationDto result = service.update(saveDto.getId(), saveDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(saveDto.getId(), result.getId()),
                () -> assertEquals(saveDto.getCountry(), result.getCountry()),
                () -> assertEquals(saveDto.getRegion(), result.getRegion()),
                () -> assertEquals(saveDto.getCity(), result.getCity()),
                () -> assertEquals(saveDto.getDistrict(), result.getDistrict()),
                () -> assertEquals(saveDto.getLocality(), result.getLocality()),
                () -> assertEquals(saveDto.getStreet(), result.getStreet()),
                () -> assertEquals(saveDto.getHouseNumber(), result.getHouseNumber()),
                () -> assertEquals(saveDto.getHouseBlock(), result.getHouseBlock()),
                () -> assertEquals(saveDto.getFlatNumber(), result.getFlatNumber()),
                () -> assertEquals(saveDto.getIndex(), result.getIndex())
        );
    }


    @Test
    @DisplayName("Обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        assertEquals("Обновление невозможно, ActualRegistration не найден!",
                assertThrows(EntityNotFoundException.class,
                        () -> service.update(-45L, new ActualRegistrationDto())).getMessage());
    }


    @Test
    @DisplayName("Поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        repository.save(new ActualRegistrationEntity(
                repository.findAll().size() + 1L, "two", "two", "two", "two",
                "two", "two", "two", "two", "two", 2L));

        repository.save(new ActualRegistrationEntity(
                repository.findAll().size() + 1L, "three", "three", "three", "three",
                "three", "three", "three", "three", "three", 3L));

        List<ActualRegistrationDto> listEntity = service.findAllById(Arrays.asList(1L, 2L, 3L));

        assertNotNull(listEntity);
        assertEquals(3, listEntity.size());
    }


    @Test
    @DisplayName("Поиск по нескольким несуществующим id, негативный сценарий")
    void findAllByNonExistIdNegativeTest() {
        List<ActualRegistrationDto> listEntity = service.findAllById(new ArrayList<>(List.of(-78L, 0L, -111L)));

        assertEquals(new ArrayList<>(), listEntity);
    }

}