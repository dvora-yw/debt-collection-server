package com.debtcollection.controller;

import com.debtcollection.dto.accountBalance.AccountBalanceCreateDto;
import com.debtcollection.dto.accountBalance.AccountBalanceDto;
import com.debtcollection.dto.accountBalance.AccountBalanceUpdateDto;
import com.debtcollection.service.AccountBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-balances")
public class AccountBalanceController {

    private final AccountBalanceService service;

    public AccountBalanceController(AccountBalanceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountBalanceDto> create(@RequestBody AccountBalanceCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountBalanceDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-end-client/{endClientId}")
    public ResponseEntity<AccountBalanceDto> byEndClient(@PathVariable Long endClientId) {
        return service.findByEndClientId(endClientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all-by-end-client/{endClientId}")
    public ResponseEntity<List<AccountBalanceDto>> allByEndClient(@PathVariable Long endClientId) {
        return ResponseEntity.ok(service.findAllByEndClientId(endClientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountBalanceDto> update(@PathVariable Long id, @RequestBody AccountBalanceUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}