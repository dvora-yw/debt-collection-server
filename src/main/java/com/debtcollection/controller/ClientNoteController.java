package com.debtcollection.controller;



import com.debtcollection.dto.clientNote.ClientNoteCreateDto;
import com.debtcollection.dto.clientNote.ClientNoteDto;
import com.debtcollection.dto.clientNote.ClientNoteUpdateDto;
import com.debtcollection.service.ClientNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/client-notes")
@RequiredArgsConstructor
public class ClientNoteController {

    private final ClientNoteService service;

    @GetMapping
    public ResponseEntity<List<ClientNoteDto>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClientNoteDto>> listByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(service.listByClientId(clientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientNoteDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ClientNoteDto> create(@RequestBody ClientNoteCreateDto createDto) {
        ClientNoteDto created = service.create(createDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientNoteDto> update(@PathVariable Long id, @RequestBody ClientNoteUpdateDto updateDto) {
        return ResponseEntity.ok(service.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}