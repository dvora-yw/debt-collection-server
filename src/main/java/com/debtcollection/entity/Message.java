package com.debtcollection.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "\"Messages\"")
public class Message {

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

    @ManyToOne
    @JoinColumn(name = "client_contact_id")
    private ClientContacts clientContact;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", length = 20)
    private MessageSource source;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", length = 20, nullable = false)
    private ContactType channel;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "status")
    private String status; // SENT / FAILED / DELIVERED
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
   }
