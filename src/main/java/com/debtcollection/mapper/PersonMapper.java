package com.debtcollection.mapper;

import com.debtcollection.dto.person.PersonCreateDto;
import com.debtcollection.dto.person.PersonDto;
import com.debtcollection.dto.person.PersonUpdateDto;
import com.debtcollection.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    Person toEntity(PersonCreateDto dto);

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    void updateEntityFromDto(
            PersonUpdateDto dto,
            @MappingTarget Person entity
    );

    // RESPONSE
    @Mapping(source = "endClient.id", target = "endClientId")
    PersonDto toResponseDto(Person entity);
}
