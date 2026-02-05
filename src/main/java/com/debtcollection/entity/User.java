package com.debtcollection.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String phone;

    private Boolean enabled = true;

    @Column(nullable = false)
    private String email;

    @Column(name = "created_by")
    private Long createdBy;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_client_id", nullable = false)
    private EndClient endClient;

    @Column(name = "is_email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "email_verification_code")
    private String emailVerificationCode;

    @Column(name = "identification_number", length = 50)
    private String identificationNumber;


}
