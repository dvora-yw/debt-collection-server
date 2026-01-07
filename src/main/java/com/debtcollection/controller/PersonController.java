package com.debtcollection.controller;

import com.debtcollection.dto.person.PersonCreateDto;
import com.debtcollection.dto.person.PersonDto;
import com.debtcollection.dto.person.PersonUpdateDto;
import com.debtcollection.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")

public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDto create(@RequestBody PersonCreateDto dto) {
        return service.create(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public PersonDto update(
            @PathVariable Long id,
            @RequestBody PersonUpdateDto dto
    ) {
        return service.update(id, dto);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public PersonDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/by-end-client/{endClientId}")
    public List<PersonDto> getAllByID(@PathVariable Long endClientId) {
        return service.getAllByID(endClientId);
    }
    // GET ALL
    @GetMapping
    public List<PersonDto> getAll() {
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
