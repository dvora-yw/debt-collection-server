package com.debtcollection.dto.notificationStep;

import com.debtcollection.entity.ContactType;
import com.debtcollection.entity.MessageSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationStepDto {
    private Long id;
    private Integer stepOrder;
    private Integer delayDays;
    private ContactType channel;
    private boolean recurring;
    private Integer recurringIntervalDays;
    private Integer recurringUntilDays;
    private String messageTemplate;
}