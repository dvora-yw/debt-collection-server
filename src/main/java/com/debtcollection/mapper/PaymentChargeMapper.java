package com.debtcollection.mapper;

import com.debtcollection.dto.paymentCharge.PaymentChargeDto;
import com.debtcollection.dto.paymentCharge.PaymentChargeCreateDto;
import com.debtcollection.dto.paymentCharge.PaymentChargeDto;
import com.debtcollection.dto.paymentCharge.PaymentChargeUpdateDto;
import com.debtcollection.entity.EndClient;
import com.debtcollection.entity.PaymentCharge;
import com.debtcollection.entity.RecurringPaymentDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentChargeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recurringPayment", source = "recurringPaymentId")
    @Mapping(target = "endClient", source = "endClientId")
    PaymentCharge toEntity(PaymentChargeCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recurringPayment", source = "recurringPaymentId")
    void updateEntityFromDto(PaymentChargeUpdateDto dto, @MappingTarget PaymentCharge entity);

    @Mapping(target = "recurringPaymentId", source = "recurringPayment.id")
    PaymentChargeDto toDto(PaymentCharge entity);

    // helper methods
    default RecurringPaymentDetails recurringPaymentFromId(Long id) {
        if (id == null) return null;
        RecurringPaymentDetails r = new RecurringPaymentDetails();
        r.setId(id);
        return r;
    }

    default EndClient endClientFromId(Long id) {
        if (id == null) return null;
        EndClient e = new EndClient();
        e.setId(id);
        return e;
    }

    default Long recurringPaymentToId(RecurringPaymentDetails r) {
        return r == null ? null : r.getId();
    }
}