package com.debtcollection.mapper;

import com.debtcollection.dto.notificationStep.NotificationStepDto;
import com.debtcollection.entity.NotificationStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NotificationStepMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "policy", ignore = true)   // יוגדר ב‑Service
    @Mapping(target = "createdAt", ignore = true)
    NotificationStep toEntity(NotificationStepDto dto);

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "policy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(
            NotificationStepDto dto,
            @MappingTarget NotificationStep entity
    );

    // RESPONSE (אין כאן שדות parent, רק נתוני השלב)
    NotificationStepDto toResponseDto(NotificationStep entity);
}