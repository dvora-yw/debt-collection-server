package com.debtcollection.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "\"EndClients\"")
public class EndClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "total_debt", precision = 10, scale = 2)
    private BigDecimal totalDebt;

    // כמה Users שייכים ל-EndClient
    @OneToMany(mappedBy = "endClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;

    // קשר ManyToMany עם Client
    @ManyToMany
    @JoinTable(
            name = "end_client_clients",
            joinColumns = @JoinColumn(name = "end_client_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "endClient")
    private List<PaymentCharge> paymentCharges;

    @OneToMany(mappedBy = "endClient")
    private List<Payment> payments;

    @OneToMany(mappedBy = "endClient")
    private List<RecurringPaymentDetails> recurringPayments;

    @OneToOne(mappedBy = "endClient")
    private AccountBalance accountBalance;
}
