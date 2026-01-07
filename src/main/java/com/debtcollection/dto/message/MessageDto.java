package com.debtcollection.dto.message;

import com.debtcollection.entity.MessageSource;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private Long clientId;
    private Long endClientId;
    private Long personId;
    private MessageSource source;
    private String content;
    LocalDateTime createdAt;


}
