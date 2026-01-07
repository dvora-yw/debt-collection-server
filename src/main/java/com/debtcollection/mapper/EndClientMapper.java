package com.debtcollection.mapper;

import com.debtcollection.dto.endClient.EndClientCreateDto;
import com.debtcollection.dto.endClient.EndClientDto;
import com.debtcollection.dto.endClient.EndClientUpdateDto;
import com.debtcollection.entity.EndClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EndClientMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    EndClient toEntity(EndClientCreateDto dto);

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    void updateEntityFromDto(
            EndClientUpdateDto dto,
            @MappingTarget EndClient entity
    );

    // RESPONSE
    @Mapping(source = "client.id", target = "clientId")
    EndClientDto toResponseDto(EndClient entity);
}
