package com.debtcollection.mapper;

import com.debtcollection.dto.client.ClientCreateDto;
import com.debtcollection.dto.client.ClientDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.ClientContacts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ClientContactMapper.class})
public interface ClientMapper {
    @Mapping(target = "id", ignore = true)
    Client toEntity(ClientCreateDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ClientDto dto, @MappingTarget Client entity);

    // כאן map User -> UserDto
    @Mapping(target = "users", source = "users")
    ClientDto toResponseDto(Client entity);}