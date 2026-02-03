package com.debtcollection.dto.message;

import com.debtcollection.entity.ContactType;
import com.debtcollection.entity.MessageSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private Long clientId;
    private Long endClientId;
    private Long userId;
    private MessageSource source;
    private String content;
    LocalDateTime createdAt;
    private ContactType channel;


}
