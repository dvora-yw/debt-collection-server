package com.debtcollection.service.outBound;

import com.debtcollection.entity.Message;
import com.debtcollection.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSender implements ChannelSender {

    private final JavaMailSender mailSender;

    @Override
    public boolean send(Message message, User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) return false;

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("התראה על חוב");
        mail.setText(message.getContent());

        mailSender.send(mail);
        return true;
    }

}

