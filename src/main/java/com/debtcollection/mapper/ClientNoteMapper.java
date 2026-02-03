package com.debtcollection.mapper;


import com.debtcollection.dto.clientNote.ClientNoteCreateDto;
import com.debtcollection.dto.clientNote.ClientNoteDto;
import com.debtcollection.dto.clientNote.ClientNoteUpdateDto;
import com.debtcollection.entity.ClientNote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientNoteMapper {

    ClientNoteDto toDto(ClientNote entity);

    ClientNote toEntity(ClientNoteCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    void updateEntityFromDto(ClientNoteUpdateDto updateDto, @MappingTarget ClientNote entity);
}