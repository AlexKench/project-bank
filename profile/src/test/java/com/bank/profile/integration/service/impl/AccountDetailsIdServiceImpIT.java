package com.bank.profile.integration.service.impl;

import com.bank.profile.dto.AccountDetailsIdDto;
import com.bank.profile.entity.ActualRegistrationEntity;
import com.bank.profile.entity.AccountDetailsIdEntity;
import com.bank.profile.entity.RegistrationEntity;
import com.bank.profile.entity.PassportEntity;
import com.bank.profile.entity.ProfileEntity;
import com.bank.profile.mapper.ProfileMapper;
import com.bank.profile.repository.AccountDetailsIdRepository;
import com.bank.profile.repository.ProfileRepository;
import com.bank.profile.service.impl.AccountDetailsIdServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Интеграционные тесты для {@link AccountDetailsIdServiceImp}
 */
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Интеграционные тесты для AccountDetailsIdServiceImp")
class AccountDetailsIdServiceImpIT {

    @Autowired
    private AccountDetailsIdServiceImp service;

    @Autowired
    private AccountDetailsIdRepository repository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper mapper;

    private AccountDetailsIdEntity entity;


    private static final RegistrationEntity registration = new RegistrationEntity(
            1L, "one", "one", "one", "one",
            "one", "one", "one", "one", "one", 1L);

    public static final ActualRegistrationEntity actualRegistration = new ActualRegistrationEntity(
            1L, "one", "one", "one", "one",
            "one", "one", "one", "one", "one", 1L);

    public static final PassportEntity passport = new PassportEntity(
            1L, 1, 1L, "one", "one",
            "one", "m", LocalDate.now(), "one", "one",
            LocalDate.now(), 1, LocalDate.now(), registration);

    public static final ProfileEntity profile = new ProfileEntity(
            1L, 1L, "one", "one",
            1L, 1L, passport, actualRegistration);


    @BeforeEach
    void createEntityForDB() {
        entity = new AccountDetailsIdEntity(1L, 1L, profile);
        profileRepository.save(profile);
        repository.save(entity);
    }


    @Test
    @DisplayName("Поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AccountDetailsIdDto result = service.findById(entity.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(entity.getId(), result.getId()),
                () -> assertEquals(entity.getAccountId(), result.getAccountId()),
                () -> assertEquals(entity.getProfile().getId(), result.getProfile().getId())
        );
    }


    @Test
    @DisplayName("Поиск по несуществующему id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        assertEquals("accountDetailsId с данным id не найден!",
                assertThrows(EntityNotFoundException.class,
                        () -> service.findById(-54L)).getMessage());
    }


    @Test
    @DisplayName("Сохранение по id, позитивный сценарий")
    void saveByIdPositiveTest() {
        AccountDetailsIdDto saveDto = new AccountDetailsIdDto(
                repository.findAll().size() + 1L, 2L, mapper.toDto(profile));

        AccountDetailsIdDto result = service.save(saveDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(saveDto.getId(), result.getId()),
                () -> assertEquals(saveDto.getAccountId(), result.getAccountId()),
                () -> assertEquals(saveDto.getProfile(), result.getProfile())
        );
    }


    @Test
    @DisplayName("Сохранение обьекта с пустыми полями, негативный сценарий")
    void saveEmptyNegativeTest() {
        assertThrows(DataIntegrityViolationException.class
                , () -> service.save(new AccountDetailsIdDto()));
    }


    @Test
    @DisplayName("Обновление по id, позитивный сценарий")
    void updateByIdPositiveTest() {
        AccountDetailsIdDto saveDto = new AccountDetailsIdDto(1L, 111L, mapper.toDto(profile));

        AccountDetailsIdDto result = service.update(1L, saveDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(saveDto.getId(), result.getId()),
                () -> assertEquals(saveDto.getAccountId(), result.getAccountId()),
                () -> assertEquals(saveDto.getProfile(), result.getProfile())
        );
    }


    @Test
    @DisplayName("Обновление по несуществующему id, негативный сценарий")
    void updateByNonExistIdNegativeTest() {
        assertEquals("Обновление невозможно, accountDetailsId не найден!",
                assertThrows(EntityNotFoundException.class,
                        () -> service.update(-45L, new AccountDetailsIdDto())).getMessage());
    }


    @Test
    @DisplayName("Поиск по нескольким id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        repository.save(new AccountDetailsIdEntity(
                repository.findAll().size() + 1L, 2L, profile));

        repository.save(new AccountDetailsIdEntity(
                repository.findAll().size() + 1L, 3L, profile));

        List<AccountDetailsIdEntity> listEntity = repository.findAll();

        assertNotNull(listEntity);
        assertEquals(3, listEntity.size());
    }


    @Test
    @DisplayName("Поиск по нескольким несуществующим id, негативный сценарий")
    void findAllByNonExistIdNegativeTest() {
        List<AccountDetailsIdDto> listEntity = service.findAllById(new ArrayList<>(List.of(-78L, 0L, -111L)));

        assertEquals(new ArrayList<>(), listEntity);
    }
}