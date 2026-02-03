package com.debtcollection.dto.clientDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class ClientDocumentCreateDto {
    private Long clientId;
    private String fileName;
    private String filePath;
    private String mimeType;   // לשימוש JSON fallback בלבד
    }