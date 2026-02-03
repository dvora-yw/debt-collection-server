package com.debtcollection.service;

import com.debtcollection.dto.clientNotificationPolicy.ClientNotificationPolicyDto;
import com.debtcollection.dto.notificationStep.NotificationStepDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.ClientNotificationPolicy;
import com.debtcollection.entity.NotificationStep;
import com.debtcollection.mapper.ClientNotificationPolicyMapper;
import com.debtcollection.mapper.NotificationStepMapper;
import com.debtcollection.repository.ClientNotificationPolicyRepository;
import com.debtcollection.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientNotificationPolicyService {

    private final ClientNotificationPolicyRepository policyRepository;
    private final ClientRepository clientRepository;
    private final ClientNotificationPolicyMapper clientNotificationPolicyMapper;
    private final NotificationStepMapper notificationStepMapper;

    @Transactional(readOnly = true)
    public ClientNotificationPolicyDto getForClient(Long clientId) {
        return policyRepository.findByClientId(clientId)
                .map(clientNotificationPolicyMapper::toResponseDto)
                .orElseGet(() -> {
                    ClientNotificationPolicyDto dto = new ClientNotificationPolicyDto();
                    dto.setClientId(clientId);
                    dto.setUseDefault(true); // אם אין מדיניות – להשתמש בברירת מחדל
                    return dto;
                });
    }

    @Transactional
    public ClientNotificationPolicyDto saveForClient(Long clientId, ClientNotificationPolicyDto dto) {
        // 1. לוודא שהלקוח קיים
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // 2. לטעון או ליצור מדיניות חדשה
        ClientNotificationPolicy policy = policyRepository.findByClientId(clientId)
                .orElseGet(ClientNotificationPolicy::new);

        // 3. תמיד לחבר את ה‑client (כדי שלא נכניס client_id = NULL)
        policy.setClient(client);

        // 4. לעדכן שדות בסיסיים מתוך ה‑DTO (בלי client ובלי steps)
        clientNotificationPolicyMapper.updateEntityFromDto(dto, policy);
        if (policy.getSteps() == null) {
            policy.setSteps(new ArrayList<>());
        }
        // 5. לנהל את רשימת השלבים בעצמנו (כמו שהקליינט שולח)
        policy.getSteps().clear();
        if (!policy.isUseDefault() && dto.getSteps() != null) {
            int order = 1;
            for (NotificationStepDto stepDto : dto.getSteps()) {
                NotificationStep step = notificationStepMapper.toEntity(stepDto);
                step.setPolicy(policy);

                // סדר השלב: לפי מה שהגיע, ואם לא – לפי המיקום ברשימה
                Integer stepOrder = stepDto.getStepOrder();
                step.setStepOrder(stepOrder != null ? stepOrder : order++);

                // לוודא שלא נכניס null לערכים נומריים הכרחיים
                if (step.getDelayDays() == null) {
                    step.setDelayDays(0);
                }

                policy.getSteps().add(step);
            }
        }

        // 6. לשמור ולהחזיר כ‑DTO
        ClientNotificationPolicy saved = policyRepository.save(policy);
        return clientNotificationPolicyMapper.toResponseDto(saved);
    }
}