package com.debtcollection.dto.clientDocument;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDocumentUpdateDto {
    private Long clientId;
    private String fileName;
    private String filePath;

}
