package com.debtcollection.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "\"PaymentCharges\"")
public class PaymentCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "end_client_id")
    private EndClient endClient;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "due_date")
    private LocalDate dueDate;
    @Column(name = "payment_method")
    private String paymentMethod; // CREDIT_CARD / BANK_TRANSFER
    @Column(nullable = false)
    private String type; // ONE_TIME / RECURRING
    private String status; // OPEN / PARTIAL / PAID / OVERDUE / TRANSFERRED

    @ManyToOne
    @JoinColumn(name = "recurring_id")
    private RecurringPaymentDetails recurringPayment;

    @OneToMany(mappedBy = "paymentCharge")
    private List<PaymentAllocation> allocations;
}
