package com.debtcollection.dto.accountBalance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceCreateDto {
    private Long endClientId;
    private BigDecimal balance;
    private String currency;
    private LocalDate asOfDate;
    private LocalDate lastUpdated;
}