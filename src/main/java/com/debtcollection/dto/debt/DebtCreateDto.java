package com.debtcollection.dto.debt;

import com.debtcollection.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
public class DebtCreateDto {
    private Long endClientId;
    private BigDecimal amount;
    private LocalDate createdAt;
    private LocalDate dueDate;
    private PaymentMethod paymentMethod;

}
