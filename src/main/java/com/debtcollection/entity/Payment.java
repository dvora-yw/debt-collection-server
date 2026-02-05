package com.debtcollection.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "Payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "end_client_id")
    private EndClient endClient;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod; // CREDIT_CARD / BANK_TRANSFER
    @Column(name = "payment_date", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "payment")
    private List<PaymentAllocation> allocations;
}
