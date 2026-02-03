package com.debtcollection.controller;


import com.debtcollection.dto.paymentAllocation.PaymentAllocationCreateDto;
import com.debtcollection.dto.paymentAllocation.PaymentAllocationDto;
import com.debtcollection.dto.paymentAllocation.PaymentAllocationUpdateDto;
import com.debtcollection.service.PaymentAllocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-allocations")
public class PaymentAllocationController {

    private final PaymentAllocationService service;

    public PaymentAllocationController(PaymentAllocationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentAllocationDto> create(@RequestBody PaymentAllocationCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentAllocationDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-payment")
    public ResponseEntity<List<PaymentAllocationDto>> byPayment(@RequestParam Long paymentId) {
        return ResponseEntity.ok(service.findByPaymentId(paymentId));
    }

    @GetMapping("/by-charge")
    public ResponseEntity<List<PaymentAllocationDto>> byCharge(@RequestParam Long paymentChargeId) {
        return ResponseEntity.ok(service.findByPaymentChargeId(paymentChargeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentAllocationDto> update(@PathVariable Long id, @RequestBody PaymentAllocationUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}