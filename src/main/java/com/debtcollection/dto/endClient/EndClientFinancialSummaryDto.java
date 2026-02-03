package com.debtcollection.dto.endClient;

import com.debtcollection.dto.accountBalance.AccountBalanceDto;
import com.debtcollection.dto.payment.PaymentDto;
import com.debtcollection.dto.paymentCharge.PaymentChargeDto;
import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class EndClientFinancialSummaryDto {
    private AccountBalanceDto accountBalance;
    private List<RecurringPaymentDetailsDto> recurringDetails;
    private List<PaymentChargeDto> openCharges;
    private List<PaymentDto> recentPayments;

}
