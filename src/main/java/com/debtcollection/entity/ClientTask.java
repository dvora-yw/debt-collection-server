package com.debtcollection.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "\"ClientTasks\"")
@Data
public class ClientTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    private String title;
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    private String status;

    @Column(name = "created_at")
    private LocalDate createdAt;
}