package com.debtcollection.mapper;

import com.debtcollection.dto.endClient.EndClientCreateDto;
import com.debtcollection.dto.endClient.EndClientDto;
import com.debtcollection.dto.endClient.EndClientUpdateDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.EndClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface EndClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clients", ignore = true)
    EndClient toEntity(EndClientCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clients", ignore = true)
    void updateEntityFromDto(EndClientUpdateDto dto, @MappingTarget EndClient entity);

    @Mapping(target = "clientIds", expression = "java(mapClientIds(entity.getClients()))")
    @Mapping(target = "users", source = "users")
    EndClientDto toResponseDto(EndClient entity);

    default Set<Long> mapClientIds(Set<Client> clients) {
        if (clients == null) return new HashSet<>();
        return clients.stream().map(Client::getId).collect(Collectors.toSet());
    }
}
