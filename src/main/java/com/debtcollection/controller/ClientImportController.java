package com.debtcollection.controller;

import com.debtcollection.service.ClientExcelImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientImportController {

    private final ClientExcelImportService excelImportService;

    @PostMapping("/import")
    public ResponseEntity<?> importClients(
            @RequestParam("file") MultipartFile file
    ) {
        excelImportService.importFromExcel(file);
        return ResponseEntity.ok("Clients imported successfully");
    }
}