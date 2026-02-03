package com.debtcollection.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "RecurringPaymentDetails")
public class RecurringPaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "end_client_id", nullable = false)
    private EndClient endClient;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "interval_value")
    private Integer intervalValue; // כל כמה יחידות
    @Column(name = "interval_unit")
    private String intervalUnit; // DAYS, MONTHS, YEARS
    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "recurringPayment")
    private List<PaymentCharge> paymentCharges;
}
