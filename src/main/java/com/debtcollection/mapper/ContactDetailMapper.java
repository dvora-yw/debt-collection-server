package com.debtcollection.mapper;

import com.debtcollection.dto.contactDetail.ContactDetailCreateDto;
import com.debtcollection.dto.contactDetail.ContactDetailDto;
import com.debtcollection.dto.contactDetail.ContactDetailUpdateDto;
import com.debtcollection.entity.ContactDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContactDetailMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    @Mapping(target = "person", ignore = true)
    ContactDetail toEntity(ContactDetailCreateDto dto);

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    @Mapping(target = "person", ignore = true)
    void updateEntityFromDto(
            ContactDetailUpdateDto dto,
            @MappingTarget ContactDetail entity
    );


    // RESPONSE
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "endClient.id", target = "endClientId")
    @Mapping(source = "person.id", target = "personId")
    ContactDetailDto toResponseDto(ContactDetail entity);
}
