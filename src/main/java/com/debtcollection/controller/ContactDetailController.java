package com.debtcollection.controller;

import com.debtcollection.dto.contactDetail.ContactDetailCreateDto;
import com.debtcollection.dto.contactDetail.ContactDetailDto;
import com.debtcollection.dto.contactDetail.ContactDetailUpdateDto;
import com.debtcollection.service.ContactDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-details")

public class ContactDetailController {

    private final ContactDetailService service;

    public ContactDetailController(ContactDetailService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDetailDto create(@RequestBody ContactDetailCreateDto dto) {
        return service.create(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ContactDetailDto update(
            @PathVariable Long id,
            @RequestBody ContactDetailUpdateDto dto
    ) {
        return service.update(id, dto);
    }

    // GET ALL
    @GetMapping
    public List<ContactDetailDto> getAll() {
        return service.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ContactDetailDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

