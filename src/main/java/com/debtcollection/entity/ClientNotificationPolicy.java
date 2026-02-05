package com.debtcollection.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "\"ClientNotificationPolicies\"")
public class ClientNotificationPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private Client client;

    // אם true – מתעלמים מהשלבים השמורים ומשתמשים בברירת המחדל הקבועה בקוד
    @Column(name = "use_default", nullable = false)
    private boolean useDefault = true;

    // אחרי כמה ימים מהחוב הראשון מעבירים לגבייה משפטית
    @Column(name = "legal_escalation_after_days")
    private Integer legalEscalationAfterDays; // ברירת מחדל 60

    // מינימום סכום חוב כדי לעבור לגבייה משפטית
    @Column(name = "legal_min_amount")
    private BigDecimal legalMinAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepOrder ASC")
    private List<NotificationStep> steps = new ArrayList<>();
    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (legalEscalationAfterDays == null) {
            legalEscalationAfterDays = 60;
        }
    }
}