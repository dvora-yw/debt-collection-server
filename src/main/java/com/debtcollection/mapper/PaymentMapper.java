package com.debtcollection.mapper;

import com.debtcollection.dto.payment.PaymentCreateDto;
import com.debtcollection.dto.payment.PaymentDto;
import com.debtcollection.dto.payment.PaymentUpdateDto;
import com.debtcollection.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    Payment toEntity(PaymentCreateDto dto);

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endClient", ignore = true)
    void updateEntityFromDto(
            PaymentUpdateDto dto,
            @MappingTarget Payment entity
    );

    // RESPONSE
    @Mapping(source = "endClient.id", target = "endClientId")
    PaymentDto toResponseDto(Payment entity);
}
