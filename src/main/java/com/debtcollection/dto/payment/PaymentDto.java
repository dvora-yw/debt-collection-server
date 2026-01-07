package com.debtcollection.dto.payment;

import com.debtcollection.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private Long endClientId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private LocalDate date;

}
