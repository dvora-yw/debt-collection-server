package com.debtcollection.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "PaymentAllocations")
public class PaymentAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_charge_id")
    private PaymentCharge paymentCharge;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amountAllocated;
}
