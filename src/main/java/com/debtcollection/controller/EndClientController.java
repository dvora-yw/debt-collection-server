package com.debtcollection.controller;

import com.debtcollection.dto.endClient.EndClientCreateDto;
import com.debtcollection.dto.endClient.EndClientDto;
import com.debtcollection.dto.endClient.EndClientFinancialSummaryDto;
import com.debtcollection.dto.endClient.EndClientUpdateDto;
import com.debtcollection.service.EndClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/end-clients")

public class EndClientController {

    private final EndClientService service;

    public EndClientController(EndClientService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EndClientDto create(@RequestBody EndClientCreateDto dto) {
        return service.create(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public EndClientDto update(
            @PathVariable Long id,
            @RequestBody EndClientUpdateDto dto
    ) {
        return service.update(id, dto);
    }

    // GET ALL
    @GetMapping
    public List<EndClientDto> getAll() {
        return service.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public EndClientDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/by-client/{clientId}")
    public List<EndClientDto> getAllById(@PathVariable Long clientId) {
        return service.getAllById(clientId);
    }
    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}/financial-summary")
    public EndClientFinancialSummaryDto getFinancialSummary(@PathVariable Long id) {
        return service.getFinancialSummary(id);
    }
}
