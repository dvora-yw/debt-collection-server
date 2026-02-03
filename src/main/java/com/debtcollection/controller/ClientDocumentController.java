package com.debtcollection.controller;


import com.debtcollection.dto.clientDocument.ClientDocumentCreateDto;
import com.debtcollection.dto.clientDocument.ClientDocumentDto;
import com.debtcollection.dto.clientDocument.ClientDocumentUpdateDto;
import com.debtcollection.dto.clientDocument.FileResource;
import com.debtcollection.service.ClientDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.charset.StandardCharsets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/client-documents")
@RequiredArgsConstructor
public class ClientDocumentController {

    private final ClientDocumentService service;

    @GetMapping
    public ResponseEntity<List<ClientDocumentDto>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClientDocumentDto>> listByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(service.listByClientId(clientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDocumentDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ClientDocumentDto> create(@RequestBody ClientDocumentCreateDto createDto) {
        ClientDocumentDto created = service.create(createDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDocumentDto> update(@PathVariable Long id, @RequestBody ClientDocumentUpdateDto updateDto) {
        return ResponseEntity.ok(service.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/upload")
    public ResponseEntity<ClientDocumentDto> upload(@RequestParam Long clientId,
                                                    @RequestParam MultipartFile file) {
        ClientDocumentDto created = service.upload(clientId, file);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}/replace")
    public ResponseEntity<ClientDocumentDto> replace(@PathVariable Long id,
                                                     @RequestParam MultipartFile file) {
        return ResponseEntity.ok(service.replaceFile(id, file));
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> inline(@PathVariable Long id) throws Exception {
        FileResource fr = service.getFileResource(id);

        HttpHeaders headers = new HttpHeaders();
        String mime = fr.getMimeType() != null ? fr.getMimeType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        headers.setContentType(MediaType.parseMediaType(mime));
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename(fr.getFileName(), StandardCharsets.UTF_8) // supports Hebrew
                        .build()
        );
        // Optional security header (literal key, not a constant):
        headers.set("X-Content-Type-Options", "nosniff");

        Resource res = fr.getResource();
        try { headers.setContentLength(res.contentLength()); } catch (Exception ignored) {}

        return new ResponseEntity<>(res, headers, HttpStatus.OK);
    }
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws Exception {
        FileResource fr = service.getFileResource(id);

        HttpHeaders headers = new HttpHeaders();
        String mime = fr.getMimeType() != null ? fr.getMimeType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        headers.setContentType(MediaType.parseMediaType(mime));
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(fr.getFileName(), StandardCharsets.UTF_8)
                        .build()
        );
        headers.set("X-Content-Type-Options", "nosniff");

        Resource res = fr.getResource();
        try { headers.setContentLength(res.contentLength()); } catch (Exception ignored) {}

        return new ResponseEntity<>(res, headers, HttpStatus.OK);
    }
}