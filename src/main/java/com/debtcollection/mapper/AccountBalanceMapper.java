package com.debtcollection.mapper;

import com.debtcollection.dto.accountBalance.AccountBalanceCreateDto;
import com.debtcollection.dto.accountBalance.AccountBalanceDto;
import com.debtcollection.dto.accountBalance.AccountBalanceUpdateDto;
import com.debtcollection.entity.AccountBalance;
import com.debtcollection.entity.EndClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountBalanceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endClient", source = "endClientId")
    AccountBalance toEntity(AccountBalanceCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endClient", source = "endClientId")
    void updateEntityFromDto(AccountBalanceUpdateDto dto, @MappingTarget AccountBalance entity);

    @Mapping(target = "endClientId", source = "endClient.id")
    AccountBalanceDto toDto(AccountBalance entity);

    // helper methods used by MapStruct to transform ids <-> stub entities
    default EndClient endClientFromId(Long id) {
        if (id == null) return null;
        EndClient ec = new EndClient();
        ec.setId(id);
        return ec;
    }

    default Long endClientToId(EndClient ec) {
        return ec == null ? null : ec.getId();
    }
}