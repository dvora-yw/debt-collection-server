package com.debtcollection.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ContactDetails")
public class ContactDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "end_client_id")
    private EndClient endClient;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private ContactType type;

    @Column(name = "value", length = 255)
    private String value;

  }
