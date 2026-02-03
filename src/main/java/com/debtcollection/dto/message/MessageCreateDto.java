package com.debtcollection.dto.message;

import com.debtcollection.entity.ContactType;
import com.debtcollection.entity.MessageSource;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageCreateDto {
    private Long clientId;
    private Long endClientId;
    private Long userId;
    private MessageSource source;
    private String content;
    private ContactType channel;

}
