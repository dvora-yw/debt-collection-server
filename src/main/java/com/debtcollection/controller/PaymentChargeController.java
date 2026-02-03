package com.debtcollection.controller;
import com.debtcollection.dto.paymentCharge.PaymentChargeCreateDto;
import com.debtcollection.dto.paymentCharge.PaymentChargeDto;
import com.debtcollection.dto.paymentCharge.PaymentChargeUpdateDto;
import com.debtcollection.service.PaymentChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-charges")
public class PaymentChargeController {

    private final PaymentChargeService service;

    public PaymentChargeController(PaymentChargeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentChargeDto> create(@RequestBody PaymentChargeCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentChargeDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentChargeDto>> findByRecurringPaymentId(@RequestParam Long recurringPaymentId) {
        return ResponseEntity.ok(service.findByRecurringPaymentId(recurringPaymentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentChargeDto> update(@PathVariable Long id, @RequestBody PaymentChargeUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}