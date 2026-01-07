package com.debtcollection.mapper;

import com.debtcollection.dto.client.ClientCreateDto;
import com.debtcollection.dto.client.ClientDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.ClientContacts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ClientContactMapper.class})
public interface ClientMapper {

    // CREATE - יצירת Client חדש
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contacts", ignore = true) // נקבע ב-Service
    Client toEntity(ClientCreateDto dto);

    // UPDATE - עדכון Client קיים
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contacts", ignore = true) // אפשר לעדכן בנפרד
    void updateEntityFromDto(ClientDto dto, @MappingTarget Client entity);

    // RESPONSE - המרת Client ל-ClientDto
    @Mapping(source = "contacts", target = "contacts")
    ClientDto toResponseDto(Client entity);
}