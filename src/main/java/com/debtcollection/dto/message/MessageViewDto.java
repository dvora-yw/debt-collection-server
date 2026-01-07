package com.debtcollection.dto.message;

import com.debtcollection.entity.MessageSource;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageViewDto {

    private Long id;
    private String content;
    private String senderName;
    private MessageSource source;
    private LocalDateTime createdAt;
}
