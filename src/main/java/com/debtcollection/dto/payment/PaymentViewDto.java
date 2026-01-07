package com.debtcollection.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PaymentViewDto {

    private Long id;
    private Long endClientId;
    private String endClientName;
    private BigDecimal amount;
    private LocalDate date;
}
