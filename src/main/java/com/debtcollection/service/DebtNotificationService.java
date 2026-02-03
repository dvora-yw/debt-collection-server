package com.debtcollection.service;

import com.debtcollection.entity.*;
import com.debtcollection.repository.ClientNotificationPolicyRepository;
import com.debtcollection.repository.MessageRepository;
import com.debtcollection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtNotificationService {

    private final ClientNotificationPolicyRepository policyRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository; // כדי למצוא משתמש של הלקוח קצה
    private final OutboundMessageSenderImpl outboundMessageSender;

    @Transactional
    public void onNewDebtCreated(Client client,
                                 EndClient endClient,
                                 Long debtId,
                                 BigDecimal debtAmount,
                                 LocalDateTime debtCreatedAt) {

        ClientNotificationPolicy policy = policyRepository.findByClientId(client.getId())
                .orElse(null);

        List<NotificationStep> effectiveSteps = buildEffectiveSteps(policy);
        if (effectiveSteps.isEmpty()) {
            return;
        }

        LocalDateTime baseTime = debtCreatedAt != null ? debtCreatedAt : LocalDateTime.now();
        List<Message> messagesToSave = new ArrayList<>();

        // למצוא את המשתמש הראשי של הלקוח קצה פעם אחת
        User endClientUser = findPrimaryEndClientUser(endClient);

        for (NotificationStep step : effectiveSteps) {
            LocalDateTime firstScheduled = baseTime.plusDays(
                    step.getDelayDays() != null ? step.getDelayDays() : 0
            );

            createMessageForStep(
                    messagesToSave, step, client, endClient, endClientUser,
                    debtId, debtAmount, firstScheduled
            );

            if (step.isRecurring()
                    && step.getRecurringIntervalDays() != null
                    && step.getRecurringIntervalDays() > 0
                    && step.getRecurringUntilDays() != null
                    && step.getRecurringUntilDays() > 0) {

                int interval = step.getRecurringIntervalDays();
                int until = step.getRecurringUntilDays();
                int currentOffset = step.getDelayDays() != null ? step.getDelayDays() : 0;

                while (currentOffset + interval <= until) {
                    currentOffset += interval;
                    LocalDateTime scheduled = baseTime.plusDays(currentOffset);

                    createMessageForStep(
                            messagesToSave, step, client, endClient, endClientUser,
                            debtId, debtAmount, scheduled
                    );
                }
            }
        }

        messageRepository.saveAll(messagesToSave);

        List<Message> saved = messageRepository.saveAll(messagesToSave);

        for (Message msg : saved) {
            if (msg.getChannel() == ContactType.EMAIL
                    && !msg.getScheduledAt().isAfter(LocalDateTime.now())) {
                outboundMessageSender.sendNow(msg);
                msg.setStatus("SENT");
            }
        }

        messageRepository.saveAll(saved);
    }

    private User findPrimaryEndClientUser(EndClient endClient) {
        return userRepository
                .findFirstByEndClientIdAndEnabledTrueOrderByIdAsc(endClient.getId())
                .orElse(null);
    }
    private void createMessageForStep(List<Message> messages,
                                      NotificationStep step,
                                      Client client,
                                      EndClient endClient,
                                      User endClientUser,
                                      Long debtId,
                                      BigDecimal amount,
                                      LocalDateTime scheduledAt) {

        Message msg = new Message();
        msg.setClient(client);
        msg.setEndClient(endClient);
        msg.setUser(endClientUser);          // יש לך יוזרים ללקוח קצה
        msg.setClientContact(null);          // אפשר להחליף בעתיד לאיש קשר
        msg.setChannel(step.getChannel());   // עכשיו יש channel גם ב-Message
        msg.setSource(MessageSource.SYSTEM); // תוודאי שזה ערך מותר ב-CK_Messages_Source
        msg.setStatus("PENDING");
        msg.setScheduledAt(scheduledAt);

        String template = step.getMessageTemplate();
        if (template == null || template.isBlank()) {
            template = buildDefaultTextForChannel(step.getChannel());
        }

        String content = template
                .replace("{{amount}}", amount != null ? amount.toPlainString() : "")
                .replace("{{paymentLink}}", "https://your-portal/pay/" + debtId);

        msg.setContent(content);

        messages.add(msg);
    }
    private String buildDefaultTextForChannel(ContactType channel) {
        return switch (channel) {
            case WHATSAPP ->
                    "שלום, נוצר עבורך חוב חדש. לתשלום: {{paymentLink}}";
            case SMS ->
                    "תזכורת לתשלום חוב. קישור לתשלום: {{paymentLink}}";
            case EMAIL ->
                    "שלום, מצורפים פרטי החוב וקישור לתשלום: {{paymentLink}}";
            default ->
                    "קיימת דרישה לתשלום חוב. לפרטים: {{paymentLink}}";
        };
    }
    private List<NotificationStep> buildEffectiveSteps(ClientNotificationPolicy policy) {
        if (policy == null || policy.isUseDefault()
                || policy.getSteps() == null || policy.getSteps().isEmpty()) {

            List<NotificationStep> defaults = new ArrayList<>();

            NotificationStep s1 = new NotificationStep();
            s1.setDelayDays(0);
            s1.setChannel(ContactType.WHATSAPP);
            s1.setRecurring(false);
            s1.setMessageTemplate("שלום, נוצר עבורך חוב חדש. לתשלום: {{paymentLink}}");
            defaults.add(s1);

            NotificationStep s2 = new NotificationStep();
            s2.setDelayDays(2);
            s2.setChannel(ContactType.SMS);
            s2.setRecurring(false);
            s2.setMessageTemplate("תזכורת לתשלום חוב. קישור לתשלום: {{paymentLink}}");
            defaults.add(s2);

            NotificationStep s3 = new NotificationStep();
            s3.setDelayDays(4);
            s3.setChannel(ContactType.VOICE_CALL);
            s3.setRecurring(false);
            defaults.add(s3);

            NotificationStep s4 = new NotificationStep();
            s4.setDelayDays(7);
            s4.setChannel(ContactType.EMAIL);
            s4.setRecurring(false);
            s4.setMessageTemplate("שלום, מצורפים פרטי החוב וקישור לתשלום: {{paymentLink}}");
            defaults.add(s4);

            NotificationStep s5 = new NotificationStep();
            s5.setDelayDays(30);
            s5.setChannel(ContactType.WHATSAPP);
            s5.setRecurring(true);
            s5.setRecurringIntervalDays(30);
            s5.setRecurringUntilDays(60);
            s5.setMessageTemplate("תזכורת נוספת לחוב שלא שולם. לתשלום: {{paymentLink}}");
            defaults.add(s5);

            return defaults;
        }

        return policy.getSteps();
    }
    // buildEffectiveSteps(...) – כמו שכבר כתבת
}