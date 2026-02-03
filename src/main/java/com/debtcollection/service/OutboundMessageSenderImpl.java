package com.debtcollection.service;

import com.debtcollection.entity.Message;
import com.debtcollection.entity.User;
import com.debtcollection.repository.OutboundMessageSender;
import com.debtcollection.repository.UserRepository;
import com.debtcollection.service.outBound.ChannelSender;
import com.debtcollection.service.outBound.ChannelSenderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboundMessageSenderImpl implements OutboundMessageSender {

    private final UserRepository userRepository;
    private final ChannelSenderFactory senderFactory;

    @Override
    @Transactional
    public void sendNow(Message message) {
        List<User> users = new ArrayList<>();

        if (message.getUser() != null) {
            users.add(message.getUser());
        } else if (message.getEndClient() != null) {
            users.addAll(userRepository.findByEndClient_IdAndEnabledTrue(
                    message.getEndClient().getId()
            ));
        }

        if (users.isEmpty()) {
            System.out.println("No users to send message " + message.getId());
            message.setStatus("FAILED");
            return;
        }

        ChannelSender sender = senderFactory.getSender(message.getChannel());
        boolean allSuccess = true;

        for (User user : users) {
            boolean success = sender.send(message, user);
            if (!success) {
                allSuccess = false;
                System.out.println("Failed to send SMS to user " + user.getId());
            }
        }

        message.setStatus(allSuccess ? "SENT" : "FAILED");
    }

}