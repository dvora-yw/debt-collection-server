package com.debtcollection.service.outBound;

import com.debtcollection.entity.ContactType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelSenderFactory {

    private final EmailSender emailSender;
    private final SmsSender smsSender;
    private final WhatsappSender whatsappSender;
    private final VoiceCallSender voiceCallSender;

    public ChannelSender getSender(ContactType channel) {
        return switch (channel) {
            case EMAIL -> emailSender;
            case SMS -> smsSender;
            case WHATSAPP -> whatsappSender;
            case VOICE_CALL -> voiceCallSender;
        };
    }
}

