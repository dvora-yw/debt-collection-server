package com.debtcollection.dto.paymentAllocation;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAllocationDto {
    private Long id;
    private Long paymentId;
    private Long paymentChargeId;
    private BigDecimal amountAllocated;
    private LocalDate allocationDate;
    private String status;
}
