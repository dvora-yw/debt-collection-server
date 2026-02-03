package com.debtcollection.dto.clientNote;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientNoteCreateDto {
    private Long clientId;
    private String content;
    private Long createdBy;
}
