package com.debtcollection.dto.clientNote;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ClientNoteDto {
    private Long id;
    private Long clientId;
    private String content;
    private LocalDateTime createdAt;
    private Long createdBy;
}