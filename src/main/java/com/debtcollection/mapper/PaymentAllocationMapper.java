package com.debtcollection.mapper;

import com.debtcollection.dto.paymentAllocation.PaymentAllocationCreateDto;
import com.debtcollection.dto.paymentAllocation.PaymentAllocationDto;
import com.debtcollection.dto.paymentAllocation.PaymentAllocationUpdateDto;
import com.debtcollection.entity.Payment;
import com.debtcollection.entity.PaymentAllocation;
import com.debtcollection.entity.PaymentCharge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentAllocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "payment", source = "paymentId")
    @Mapping(target = "paymentCharge", source = "paymentChargeId")
    PaymentAllocation toEntity(PaymentAllocationCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "payment", source = "paymentId")
    @Mapping(target = "paymentCharge", source = "paymentChargeId")
    void updateEntityFromDto(PaymentAllocationUpdateDto dto, @MappingTarget PaymentAllocation entity);

    @Mapping(target = "paymentId", source = "payment.id")
    @Mapping(target = "paymentChargeId", source = "paymentCharge.id")
    PaymentAllocationDto toDto(PaymentAllocation entity);

    // helpers
    default Payment paymentFromId(Long id) {
        if (id == null) return null;
        Payment p = new Payment();
        p.setId(id);
        return p;
    }

    default PaymentCharge paymentChargeFromId(Long id) {
        if (id == null) return null;
        PaymentCharge pc = new PaymentCharge();
        pc.setId(id);
        return pc;
    }

    default Long paymentToId(Payment p) {
        return p == null ? null : p.getId();
    }

    default Long paymentChargeToId(PaymentCharge pc) {
        return pc == null ? null : pc.getId();
    }
}