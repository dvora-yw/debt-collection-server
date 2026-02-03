package com.debtcollection.mapper;

import com.debtcollection.dto.clientNotificationPolicy.ClientNotificationPolicyDto;
import com.debtcollection.entity.ClientNotificationPolicy;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = NotificationStepMapper.class)
public interface ClientNotificationPolicyMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)   // יוגדר ב‑Service
    ClientNotificationPolicy toEntity(ClientNotificationPolicyDto dto);

    // UPDATE (partial, בלי למחוק client ו‑id)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "steps", ignore = true)
    void updateEntityFromDto(
            ClientNotificationPolicyDto dto,
            @MappingTarget ClientNotificationPolicy entity
    );

    // RESPONSE
    @Mapping(source = "client.id", target = "clientId")
    ClientNotificationPolicyDto toResponseDto(ClientNotificationPolicy entity);
}