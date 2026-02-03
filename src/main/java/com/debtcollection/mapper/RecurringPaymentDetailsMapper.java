package com.debtcollection.mapper;

import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsCreateDto;
import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsDto;
import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsUpdateDto;
import com.debtcollection.entity.EndClient;
import com.debtcollection.entity.PaymentCharge;
import com.debtcollection.entity.RecurringPaymentDetails;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecurringPaymentDetailsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endClient", source = "endClientId") // <-- הוספה
    RecurringPaymentDetails toEntity(RecurringPaymentDetailsCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RecurringPaymentDetailsUpdateDto dto, @MappingTarget RecurringPaymentDetails entity);

    @Mapping(target = "endClientId", source = "endClient.id")
    RecurringPaymentDetailsDto toDto(RecurringPaymentDetails entity);

    default EndClient endClientFromId(Long id) {
        if (id == null) return null;
        EndClient ec = new EndClient();
        ec.setId(id);
        return ec;
    }

    default Long endClientToId(EndClient ec) {
        return ec == null ? null : ec.getId();
    }

    // helpers for list of PaymentCharge ids <-> entities
    default List<PaymentCharge> paymentChargesFromIds(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(this::paymentChargeFromId).collect(Collectors.toList());
    }


    default List<Long> paymentChargesToIds(List<PaymentCharge> pcs) {
        if (pcs == null) return null;
        return pcs.stream().map(this::paymentChargeToId).collect(Collectors.toList());
    }

    default PaymentCharge paymentChargeFromId(Long id) {
        if (id == null) return null;
        PaymentCharge pc = new PaymentCharge();
        pc.setId(id);
        return pc;
    }

    default Long paymentChargeToId(PaymentCharge pc) {
        return pc == null ? null : pc.getId();
    }
}