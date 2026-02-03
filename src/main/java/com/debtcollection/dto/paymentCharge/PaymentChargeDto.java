package com.debtcollection.dto.paymentCharge;

import com.debtcollection.entity.EndClient;
import com.debtcollection.entity.PaymentAllocation;
import com.debtcollection.entity.RecurringPaymentDetails;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentChargeDto {
    private Long id;
    private Long endClientId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private String status;
    private Long recurringPaymentId;
    private LocalDate createdAt;
    private String paymentMethod; // CREDIT_CARD / BANK_TRANSFER
    private String type; // ONE_TIME / RECURRING


}