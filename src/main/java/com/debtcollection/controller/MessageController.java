package com.debtcollection.controller;

import com.debtcollection.dto.message.MessageCreateDto;
import com.debtcollection.dto.message.MessageDto;
import com.debtcollection.dto.message.MessageUpdateDto;
import com.debtcollection.dto.message.MessageViewDto;
import com.debtcollection.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")

public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDto create(@RequestBody MessageCreateDto dto) {
        return service.create(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public MessageDto update(
            @PathVariable Long id,
            @RequestBody MessageUpdateDto dto
    ) {
        return service.update(id, dto);
    }

    // GET ALL
    @GetMapping
    public List<MessageDto> getAll() {
        return service.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public MessageDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/messages")
    public List<MessageViewDto> getMessages() {
        return service.getMessages();
    }
}
