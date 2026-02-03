package com.debtcollection.dto.paymentAllocation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAllocationUpdateDto {
    private Long paymentId;
    private Long paymentChargeId;
    private BigDecimal amountAllocated;
    private LocalDate allocationDate;
    private String status;
}