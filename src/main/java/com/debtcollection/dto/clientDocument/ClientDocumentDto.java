package com.debtcollection.dto.clientDocument;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ClientDocumentDto {
    private Long id;
    private Long clientId;
    private String fileName;
    private String contentType;
    private Long size;
    private String filePath;
    private Instant uploadedAt;


}