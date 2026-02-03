package com.debtcollection.dto.clientDocument;

import org.springframework.core.io.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResource {
    private final Resource resource;
    private final String fileName;
    private final String mimeType;


}
