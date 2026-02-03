package com.debtcollection.mapper;

import com.debtcollection.dto.message.MessageCreateDto;
import com.debtcollection.dto.message.MessageDto;
import com.debtcollection.dto.message.MessageUpdateDto;
import com.debtcollection.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    @Mapping(target = "user", ignore = true)
    Message toEntity(MessageCreateDto dto);

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(
            MessageUpdateDto dto,
            @MappingTarget Message entity
    );

    // RESPONSE
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "endClient.id", target = "endClientId")
    @Mapping(source = "user.id", target = "userId")
    MessageDto toResponseDto(Message entity);
}
