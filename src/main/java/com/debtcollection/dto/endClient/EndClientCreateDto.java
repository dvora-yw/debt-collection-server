package com.debtcollection.dto.endClient;

import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndClientCreateDto {
    private Long clientId;
    private String name;
    private BigDecimal totalDebt;
    private List<UserDto> users;

    // הגדרת חיוב ראשוני
    private BigDecimal initialChargeAmount;
    private LocalDate initialChargeDueDate;
    private String initialChargeType; // ONE_TIME / RECURRING

    // פרטי חיוב מחזורי (אם initialChargeType = RECURRING)
    private Integer intervalValue;    // למשל 1
    private String intervalUnit;      // MONTHS / YEARS
    private LocalDate recurringEndDate; // אופציונלי, יכול להיות null

    // יתרת זכות התחלתית (אם יש)
    private BigDecimal initialPrepaidBalance;
    private LocalDate startDate;


}
