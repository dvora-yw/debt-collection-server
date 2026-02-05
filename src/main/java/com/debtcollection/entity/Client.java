package com.debtcollection.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "\"Clients\"")
public class Client {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        // שם לקוח / חברה
        @Column(name = "name",nullable = false)
        private String name;

        // סוג ישות (עוסק, חברה, אדם פרטי וכו')
        @Enumerated(EnumType.STRING)
        @Column(name = "entity_type", nullable = false, length = 30)
        private EntityType entityType;

        // ת"ז / ח.פ / מספר עמותה
        @Column(name = "identification_number", nullable = false, length = 20)
        private String identificationNumber;

        @Column(length = 500)
        private String address;

        @Column(length = 50)
        private String phone;

        @Column(length = 255)
        private String email;

        @Column(length = 50)
        private String fax;

        @Column(columnDefinition = "nvarchar(max)")
        private String notes;

        @Column(name = "established_date")
        private LocalDate establishedDate;

        // מספר עוסק (רק אם רלוונטי)
        @Column(name = "vat_number", length = 20)
        private String vatNumber;

        // תשלומים
        @Column(name = "payment_model", length = 50)
        private String paymentModel;

        @Column(name = "payment_terms", length = 50)
        private String paymentTerms;

        @OneToMany(mappedBy = "client")
        private List<User> users;

        // קשר ללקוחות קצה (דו־כיווני)
        @ManyToMany(mappedBy = "clients")
        private Set<EndClient> endClients;



}









