package com.debtcollection.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "NotificationSteps")
public class NotificationStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // שייכות למדיניות של לקוח
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private ClientNotificationPolicy policy;

    // סדר שלב
    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    // תוך כמה ימים מהחוב (או מהשלב הקודם) שולחים
    @Column(name = "delay_days", nullable = false)
    private Integer delayDays;

    // ערוץ ההודעה
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", length = 20, nullable = false)
    private ContactType channel;

    // האם זה שלב מחזורי (כמו וואטסאפ כל חודש)
    @Column(name = "is_recurring", nullable = false)
    private boolean recurring = false;

    // כל כמה ימים לחזור (אם recurring = true)
    @Column(name = "recurring_interval_days")
    private Integer recurringIntervalDays;

    // עד כמה ימים מהחוב להמשיך לחזור (למשל 60)
    @Column(name = "recurring_until_days")
    private Integer recurringUntilDays;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "message_template", columnDefinition = "TEXT")
    private String messageTemplate;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}