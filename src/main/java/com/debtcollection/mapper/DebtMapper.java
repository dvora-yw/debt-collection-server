package com.debtcollection.mapper;

import com.debtcollection.dto.debt.DebtCreateDto;
import com.debtcollection.dto.debt.DebtDto;
import com.debtcollection.dto.debt.DebtUpdateDto;
import com.debtcollection.entity.Debt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DebtMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    Debt toEntity(DebtCreateDto dto);

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(
            DebtUpdateDto dto,
            @MappingTarget Debt entity
    );

    // RESPONSE
    @Mapping(source = "endClient.id", target = "endClientId")
    DebtDto toResponseDto(Debt entity);
}
