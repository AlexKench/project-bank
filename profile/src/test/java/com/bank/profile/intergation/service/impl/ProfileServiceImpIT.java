package com.bank.profile.intergation.service.impl;

import com.bank.profile.dto.ProfileDto;
import com.bank.profile.entity.ActualRegistrationEntity;
import com.bank.profile.entity.ProfileEntity;
import com.bank.profile.entity.RegistrationEntity;
import com.bank.profile.entity.PassportEntity;
import com.bank.profile.mapper.ProfileMapper;
import com.bank.profile.repository.ProfileRepository;
import com.bank.profile.service.impl.ProfileServiceImp;
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
 * Интеграционные тесты для {@link ProfileServiceImp}
 */
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("Интеграционные тесты для ProfileServiceImp")
class ProfileServiceImpIT {

    @Autowired
    private ProfileServiceImp service;

    @Autowired
    private ProfileRepository repository;

    @Autowired
    private ProfileMapper profileMapper;


    private RegistrationEntity registrationEntity;

    private ActualRegistrationEntity actualRegistrationEntity;

    private PassportEntity passportEntity;


    @BeforeEach
    void create() {
        registrationEntity = new RegistrationEntity();
        registrationEntity.setIndex(5555L);
        registrationEntity.setCountry("Moskva");

        actualRegistrationEntity = new ActualRegistrationEntity();
        actualRegistrationEntity.setIndex(5555L);
        actualRegistrationEntity.setCountry("Moskva");

        passportEntity = new PassportEntity();
        passportEntity.setSeries(777);
        passportEntity.setBirthDate(LocalDate.now());
        passportEntity.setGender("m");
        passportEntity.setBirthPlace("hel");
        passportEntity.setDivisionCode(85555);
        passportEntity.setDateOfIssue(LocalDate.now());
        passportEntity.setFirstName("hel");
        passportEntity.setIssuedBy("hel");
        passportEntity.setLastName("hel");
        passportEntity.setMiddleName("hel");
        passportEntity.setNumber(88888L);
        passportEntity.setExpirationDate(LocalDate.now());
        passportEntity.setRegistration(registrationEntity);
    }


    @Test
    @DisplayName("Поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        ProfileEntity entity = new ProfileEntity(repository.findAll().size() + 1L, 1L, "hello", "hello",
                777L, 777L, passportEntity, actualRegistrationEntity);

        repository.save(entity);

        ProfileDto result = service.findById(entity.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(entity.getId(), result.getId()),
                () -> assertEquals(entity.getPhoneNumber(), result.getPhoneNumber()),
                () -> assertEquals(entity.getEmail(), result.getEmail()),
                () -> assertEquals(entity.getNameOnCard(), result.getNameOnCard()),
                () -> assertEquals(entity.getInn(), result.getInn()),
                () -> assertEquals(entity.getSnils(), result.getSnils())
        );
    }


    @Test
    @DisplayName("Поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        assertEquals("profile с данным id не найден!",
                assertThrows(EntityNotFoundException.class,
                        () -> service.findById(-54L)).getMessage());
    }


    @Test
    @DisplayName("Сохранение по id, позитивный сценарий")
    void saveByIdPositiveTest() {
        ProfileEntity entity = new ProfileEntity(repository.findAll().size() + 1L, 2L, "hello", "hello",
                778324536377L, 34542445L, passportEntity, actualRegistrationEntity);


        ProfileDto result = service.save(profileMapper.toDto(entity));

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(entity.getId(), result.getId()),
                () -> assertEquals(entity.getPhoneNumber(), result.getPhoneNumber()),
                () -> assertEquals(entity.getEmail(), result.getEmail()),
                () -> assertEquals(entity.getNameOnCard(), result.getNameOnCard()),
                () -> assertEquals(entity.getInn(), result.getInn()),
                () -> assertEquals(entity.getSnils(), result.getSnils())
        );
    }


    @Test
    @DisplayName("Сохранение обьекта с пустыми полями, негативный сценарий")
    void saveEmptyNegativeTest() {
        assertThrows(DataIntegrityViolationException.class
                , () -> service.save(new ProfileDto()));
    }


    @Test
    @DisplayName("Обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        ProfileEntity entity = new ProfileEntity(repository.findAll().size() + 1L, 153522L, "hello", "hello",
                45125845L, 41258451L, passportEntity, actualRegistrationEntity);

        repository.save(entity);

        ProfileEntity profileEntity = new ProfileEntity(entity.getId(), 256454L, "update", "update",
                45251L, 542121495L, passportEntity, actualRegistrationEntity);

        passportEntity.setId(1L);
        registrationEntity.setId(1L);
        actualRegistrationEntity.setId(1L);


        ProfileDto result = service.update(entity.getId(), profileMapper.toDto(profileEntity));

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(profileEntity.getId(), result.getId()),
                () -> assertEquals(profileEntity.getPhoneNumber(), result.getPhoneNumber()),
                () -> assertEquals(profileEntity.getEmail(), result.getEmail()),
                () -> assertEquals(profileEntity.getNameOnCard(), result.getNameOnCard()),
                () -> assertEquals(profileEntity.getInn(), result.getInn()),
                () -> assertEquals(profileEntity.getSnils(), result.getSnils())
        );
    }


    @Test
    @DisplayName("Обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        assertEquals("Обновление невозможно, profile не найден!",
                assertThrows(EntityNotFoundException.class,
                        () -> service.update(-45L, new ProfileDto())).getMessage());
    }


    @Test
    @DisplayName("Поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        ProfileEntity entity = new ProfileEntity(repository.findAll().size() + 1L, 14621L, "gggg", "gggg",
                451245L, 412581L, passportEntity, actualRegistrationEntity);

        ProfileEntity entity1 = new ProfileEntity(repository.findAll().size() + 1L, 24321L, "hhh", "hhh",
                45256351L, 542114495L, passportEntity, actualRegistrationEntity);

        ProfileEntity entity2 = new ProfileEntity(repository.findAll().size() + 1L, 1462177L, "cccc", "cccc",
                451277745L, 41258177L, passportEntity, actualRegistrationEntity);

        repository.save(entity);
        repository.save(entity1);
        repository.save(entity2);

        List<ProfileDto> result = service.findAllById(Arrays.asList(1L, 2L, 3L));

        assertEquals(3, result.size());


    }


    @Test
    @DisplayName("Поиск по нескольким несуществующим id, негативный сценарий")
    void findAllByNonExistIdNegativeTest() {
        List<ProfileDto> listEntity = service.findAllById(new ArrayList<>(List.of(-78L, 0L, -111L)));

        assertEquals(new ArrayList<>(), listEntity);
    }

}