package com.debtcollection.dto.paymentCharge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentChargeCreateDto {
    private Long endClientId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private String status;
    private Long recurringPaymentId;
    private LocalDate createdAt;
    private String paymentMethod; // CREDIT_CARD / BANK_TRANSFER
    private String type; // ONE_TIME / RECURRING
}
