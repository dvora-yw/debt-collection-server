package com.debtcollection.mapper;

import com.debtcollection.dto.clientDocument.ClientDocumentCreateDto;
import com.debtcollection.dto.clientDocument.ClientDocumentDto;
import com.debtcollection.dto.clientDocument.ClientDocumentUpdateDto;
import com.debtcollection.entity.ClientDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")

public interface ClientDocumentMapper {

    ClientDocumentDto toDto(ClientDocument entity);

    ClientDocument toEntity(ClientDocumentCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true) // preserve original upload time
    void updateEntityFromDto(ClientDocumentUpdateDto updateDto, @MappingTarget ClientDocument entity);

}
