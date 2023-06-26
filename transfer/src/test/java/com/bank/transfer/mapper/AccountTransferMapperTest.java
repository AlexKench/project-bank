package com.bank.transfer.mapper;

import com.bank.transfer.dto.AccountTransferDto;
import com.bank.transfer.entity.AccountTransferEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AccountTransferMapperTest {

    private final AccountTransferMapper mapper = Mappers.getMapper(AccountTransferMapper.class);

    AccountTransferDto dto;
    AccountTransferEntity entity;

    @BeforeEach
    void init() {
        dto = new AccountTransferDto();
        dto.setAmount(new BigDecimal("100.00"));
        dto.setAccountNumber(1234567890L);
        dto.setPurpose("Test transfer");
        dto.setAccountDetailsId(1L);

        entity = new AccountTransferEntity();
        entity.setAmount(new BigDecimal("200.00"));
        entity.setAccountNumber(9876543210L);
        entity.setPurpose("Another test transfer");
        entity.setAccountDetailsId(1L);
    }

    @Test
    @DisplayName("маппинг в энтити")
    void toEntityTest() {
        AccountTransferEntity result = mapper.toEntity(dto);

        assertThat(result).isNotNull();
        assertThat(dto.getId()).isEqualTo(result.getId());
        assertThat(dto.getAccountNumber()).isEqualTo(result.getAccountNumber());
        assertThat(dto.getAmount()).isEqualTo(result.getAmount());
        assertThat(dto.getPurpose()).isEqualTo(result.getPurpose());
        assertThat(dto.getAccountDetailsId()).isEqualTo(result.getAccountDetailsId());
    }

    @Test
    @DisplayName("маппинг в энтити, на вход подан null")
    public void toEntityNullTest() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("маппинг в дто")
    void toDtoTest() {
        AccountTransferDto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getAccountNumber()).isEqualTo(entity.getAccountNumber());
        assertThat(dto.getAmount()).isEqualTo(entity.getAmount());
        assertThat(dto.getPurpose()).isEqualTo(entity.getPurpose());
        assertThat(dto.getAccountDetailsId()).isEqualTo(entity.getAccountDetailsId());
    }

    @Test
    @DisplayName("маппинг в дто, на вход подан null")
    public void toDtoNullTest() {
        assertNull(mapper.toDto(null));
    }

    @Test
    @DisplayName("слияние в энтити")
    void mergeToEntityTest() {
        assertAll(
                () -> assertNotNull(mapper.mergeToEntity(dto, entity)),
                () -> assertEquals(mapper.mergeToEntity(dto, entity), entity)
        );
    }

    @Test
    @DisplayName("слияние в энтити, на вход подан null")
    public void mergeToEntityNullTest() {
        assertEquals(mapper.mergeToEntity(null, entity), entity);
    }

    @Test
    @DisplayName("маппинг в список дто")
    void toListDtoTest() {
        List<AccountTransferDto> result = mapper.toDtoList(new ArrayList<>(List.of(entity, entity, entity)));

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(entity.getId(), result.get(0).getId()),
                () -> assertEquals(entity.getId(), result.get(1).getId()),
                () -> assertEquals(entity.getId(), result.get(2).getId())
        );
    }

    @Test
    @DisplayName("маппинг в список дто, на вход подан null")
    public void toEmptyListDtoTest() {
        assertNull(mapper.toDtoList(null));
    }
}