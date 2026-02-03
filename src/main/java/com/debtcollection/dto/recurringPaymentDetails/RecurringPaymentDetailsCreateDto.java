package com.debtcollection.dto.recurringPaymentDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class RecurringPaymentDetailsCreateDto {
    private Long endClientId;
    private LocalDate startDate;
    private Integer intervalValue;
    private String intervalUnit; // DAYS, MONTHS, YEARS
    private LocalDate endDate;
    private List<Long> paymentChargeIds;
}
