package com.debtcollection.controller;

import com.debtcollection.service.EndClientExcelImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/end-clients")
@RequiredArgsConstructor
public class EndClientImportController {

    private final EndClientExcelImportService importService;

  /*  @PostMapping("/import")
    public ResponseEntity<?> importEndClients(
            @RequestParam("file") MultipartFile file
    ) {
        importService.importFromExcel(file);
        return ResponseEntity.ok("End clients imported successfully");
    }*/

    @PostMapping("/import")
    public ResponseEntity<?> importEndClients(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "clientId", required = false) Long clientId) {
        try {
            importService.importFromExcel(file);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}
