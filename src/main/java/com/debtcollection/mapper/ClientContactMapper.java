package com.debtcollection.mapper;

import com.debtcollection.dto.contact.ClientContactDto;
import com.debtcollection.entity.ClientContacts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface ClientContactMapper {
    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true) // נקבע ב-Service
    ClientContacts toEntity(ClientContactDto dto);

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    void updateEntityFromDto(
            ClientContactDto dto,
            @MappingTarget ClientContacts entity
    );

    // RESPONSE
    @Mapping(source = "client.id", target = "clientId")
    ClientContactDto toResponseDto(ClientContacts entity);
}
