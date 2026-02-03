package com.debtcollection.dto.recurringPaymentDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringPaymentDetailsDto {
    private Long id;
    private Long endClientId;
    private LocalDate startDate;
    private Integer intervalValue;
    private String intervalUnit; // DAYS, MONTHS, YEARS
    private LocalDate endDate;
    private List<Long> paymentChargeIds;
}