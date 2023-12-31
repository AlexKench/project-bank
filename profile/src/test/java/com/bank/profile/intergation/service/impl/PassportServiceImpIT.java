package com.bank.profile.intergation.service.impl;

import com.bank.profile.dto.PassportDto;
import com.bank.profile.entity.RegistrationEntity;
import com.bank.profile.entity.PassportEntity;
import com.bank.profile.mapper.RegistrationMapper;
import com.bank.profile.repository.PassportRepository;
import com.bank.profile.repository.RegistrationRepository;
import com.bank.profile.service.impl.PassportServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Интеграционные тесты для {@link PassportServiceImp}
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для PassportServiceImp")
class PassportServiceImpIT {


    @Autowired
    private PassportServiceImp service;

    @Autowired
    private PassportRepository repository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private RegistrationMapper mapper;

    private PassportEntity entity;

    private RegistrationEntity registration;


    @BeforeEach
    void createEntityForDB() {
        registration = new RegistrationEntity();
        registration.setCountry("one");
        registration.setIndex(18654L);

        entity = new PassportEntity(
                repository.findAll().size() + 1L, 1754, 154L, "one", "one",
                "one", "m", LocalDate.now(), "one", "one",
                LocalDate.now(), 1, LocalDate.now(), registration);

        registrationRepository.save(registration);
        repository.save(entity);
    }


    @Test
    @DisplayName("Поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        PassportDto result = service.findById(entity.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(entity.getId(), result.getId()),
                () -> assertEquals(entity.getSeries(), result.getSeries()),
                () -> assertEquals(entity.getNumber(), result.getNumber()),
                () -> assertEquals(entity.getLastName(), result.getLastName()),
                () -> assertEquals(entity.getFirstName(), result.getFirstName()),
                () -> assertEquals(entity.getMiddleName(), result.getMiddleName()),
                () -> assertEquals(entity.getGender(), result.getGender()),
                () -> assertEquals(entity.getBirthDate(), result.getBirthDate()),
                () -> assertEquals(entity.getBirthPlace(), result.getBirthPlace()),
                () -> assertEquals(entity.getDivisionCode(), result.getDivisionCode()),
                () -> assertEquals(entity.getIssuedBy(), result.getIssuedBy()),
                () -> assertEquals(registrationRepository.findAll().size(), result.getRegistration().getId())
        );
    }


    @Test
    @DisplayName("Поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        assertEquals("passport с данным id не найден!",
                assertThrows(EntityNotFoundException.class,
                        () -> service.findById(-54L)).getMessage());
    }


    @Test
    @DisplayName("Сохранение по id, позитивный сценарий")
    void saveByIdPositiveTest() {
        RegistrationEntity saveRegistration = new RegistrationEntity();
        saveRegistration.setCountry("two");
        saveRegistration.setIndex(2L);

        PassportDto saveDto = new PassportDto(
                repository.findAll().size() + 1L, 2852, 4442L, "two", "two",
                "two", "m", LocalDate.now(), "two", "two",
                LocalDate.now(), 255, LocalDate.now(), mapper.toDto(saveRegistration));

        PassportDto result = service.save(saveDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(saveDto.getId(), result.getId()),
                () -> assertEquals(saveDto.getSeries(), result.getSeries()),
                () -> assertEquals(saveDto.getNumber(), result.getNumber()),
                () -> assertEquals(saveDto.getLastName(), result.getLastName()),
                () -> assertEquals(saveDto.getFirstName(), result.getFirstName()),
                () -> assertEquals(saveDto.getMiddleName(), result.getMiddleName()),
                () -> assertEquals(saveDto.getGender(), result.getGender()),
                () -> assertEquals(saveDto.getBirthDate(), result.getBirthDate()),
                () -> assertEquals(saveDto.getBirthPlace(), result.getBirthPlace()),
                () -> assertEquals(saveDto.getDivisionCode(), result.getDivisionCode()),
                () -> assertEquals(saveDto.getIssuedBy(), result.getIssuedBy()),
                () -> assertEquals(registrationRepository.findAll().size(), result.getRegistration().getId())
        );
    }


    @Test
    @DisplayName("Сохранение обьекта с пустыми полями, негативный сценарий")
    void saveEmptyNegativeTest() {
        assertThrows(DataIntegrityViolationException.class
                , () -> service.save(new PassportDto()));
    }


    @Test
    @DisplayName("Обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        RegistrationEntity registration = new RegistrationEntity();
        registration.setId(1L);
        registration.setCountry("one");
        registration.setIndex(18654L);

        PassportDto saveDto = new PassportDto(
                1L, 146243, 1777755L, "update", "update",
                "update", "m", LocalDate.now(), "update", "update",
                LocalDate.now(), 6542323, LocalDate.now(), mapper.toDto(registration));

        PassportDto result = service.update(saveDto.getId(), saveDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(saveDto.getId(), result.getId()),
                () -> assertEquals(saveDto.getSeries(), result.getSeries()),
                () -> assertEquals(saveDto.getNumber(), result.getNumber()),
                () -> assertEquals(saveDto.getLastName(), result.getLastName()),
                () -> assertEquals(saveDto.getFirstName(), result.getFirstName()),
                () -> assertEquals(saveDto.getMiddleName(), result.getMiddleName()),
                () -> assertEquals(saveDto.getGender(), result.getGender()),
                () -> assertEquals(saveDto.getBirthDate(), result.getBirthDate()),
                () -> assertEquals(saveDto.getBirthPlace(), result.getBirthPlace()),
                () -> assertEquals(saveDto.getDivisionCode(), result.getDivisionCode()),
                () -> assertEquals(saveDto.getIssuedBy(), result.getIssuedBy()),
                () -> assertEquals(saveDto.getRegistration(), result.getRegistration())
        );
    }


    @Test
    @DisplayName("Обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        assertEquals("Обновление невозможно, passport не найден!",
                assertThrows(EntityNotFoundException.class,
                        () -> service.update(-45L, new PassportDto())).getMessage());
    }


    @Test
    @DisplayName("Поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        PassportEntity entity = new PassportEntity(
                repository.findAll().size() + 1L, 1754, 154L, "one", "one",
                "one", "m", LocalDate.now(), "one", "one",
                LocalDate.now(), 1, LocalDate.now(), registration);

        PassportEntity entity1 = new PassportEntity(
                repository.findAll().size() + 1L, 1754, 154L, "one", "one",
                "one", "m", LocalDate.now(), "one", "one",
                LocalDate.now(), 1, LocalDate.now(), registration);

        PassportEntity entity2 = new PassportEntity(
                repository.findAll().size() + 1L, 5565456, 154L, "one", "one",
                "one", "m", LocalDate.now(), "one", "one",
                LocalDate.now(), 1, LocalDate.now(), registration);

        registrationRepository.save(registration);
        repository.save(entity);
        repository.save(entity1);
        repository.save(entity2);

        List<PassportDto> listDto = service.findAllById(Arrays.asList(1L, 2L, 3L));


        assertNotNull(listDto);
        assertEquals(3, listDto.size());
    }


    @Test
    @DisplayName("Поиск по нескольким несуществующим id, негативный сценарий")
    void findAllByNonExistIdNegativeTest() {
        List<PassportDto> listEntity = service.findAllById(new ArrayList<>(List.of(-78L, 0L, -111L)));

        assertEquals(new ArrayList<>(), listEntity);
    }

}