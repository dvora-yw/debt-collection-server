package com.debtcollection.controller;

import com.debtcollection.dto.debt.DebtCreateDto;
import com.debtcollection.dto.debt.DebtDto;
import com.debtcollection.dto.debt.DebtUpdateDto;
import com.debtcollection.service.DebtService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/debts")

public class DebtController {

    private final DebtService service;

    public DebtController(DebtService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DebtDto create(@RequestBody DebtCreateDto dto) {
        return service.create(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public DebtDto update(
            @PathVariable Long id,
            @RequestBody DebtUpdateDto dto
    ) {
        return service.update(id, dto);
    }

    // GET ALL
    @GetMapping
    public List<DebtDto> getAll() {
        return service.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public DebtDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
