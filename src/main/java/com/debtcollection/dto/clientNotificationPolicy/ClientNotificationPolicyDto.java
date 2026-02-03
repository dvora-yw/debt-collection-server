package com.debtcollection.dto.clientNotificationPolicy;


import com.debtcollection.dto.notificationStep.NotificationStepDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientNotificationPolicyDto {

    private Long id;
    private Long clientId;
    private boolean useDefault;
    private Integer legalEscalationAfterDays;
    private BigDecimal legalMinAmount;
    private List<NotificationStepDto> steps;
}
