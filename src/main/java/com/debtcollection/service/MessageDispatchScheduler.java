package com.debtcollection.service;

import com.debtcollection.entity.Message;
import com.debtcollection.repository.MessageRepository;
import com.debtcollection.repository.OutboundMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageDispatchScheduler {

    private final MessageRepository messageRepository;
    private final OutboundMessageSender outboundMessageSender;

    @Scheduled(fixedDelay = 10_000)
    @Transactional
    public void dispatchPending() {
        LocalDateTime now = LocalDateTime.now();

        List<Message> messages =
                messageRepository.findTop100ByStatusAndScheduledAtLessThanEqualOrderByScheduledAtAsc(
                        "PENDING", now
                );

        for (Message msg : messages) {
            try {
                outboundMessageSender.sendNow(msg);
                System.out.println("Message " + msg.getId() + " dispatched with status " + msg.getStatus());
            } catch (Exception ex) {
                ex.printStackTrace();
                msg.setStatus("FAILED");
            }
        }
    }

}