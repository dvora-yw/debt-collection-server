package com.debtcollection.controller;
import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsCreateDto;
import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsDto;
import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsUpdateDto;
import com.debtcollection.service.RecurringPaymentDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring-payment-details")
public class RecurringPaymentDetailsController {

    private final RecurringPaymentDetailsService service;

    public RecurringPaymentDetailsController(RecurringPaymentDetailsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RecurringPaymentDetailsDto> create(@RequestBody RecurringPaymentDetailsCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecurringPaymentDetailsDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RecurringPaymentDetailsDto>> findByEndClientId(@RequestParam Long endClientId) {
        return ResponseEntity.ok(service.findByEndClientId(endClientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringPaymentDetailsDto> update(@PathVariable Long id,
                                                             @RequestBody RecurringPaymentDetailsUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}