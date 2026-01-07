package com.debtcollection.controller;

import com.debtcollection.dto.payment.PaymentCreateDto;
import com.debtcollection.dto.payment.PaymentDto;
import com.debtcollection.dto.payment.PaymentUpdateDto;
import com.debtcollection.dto.payment.PaymentViewDto;
import com.debtcollection.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")

public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto create(@RequestBody PaymentCreateDto dto) {
        return service.create(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public PaymentDto update(
            @PathVariable Long id,
            @RequestBody PaymentUpdateDto dto
    ) {
        return service.update(id, dto);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public PaymentDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<PaymentDto> getAll() {
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/view")
    public List<PaymentViewDto> getPaymentsForView() {
        return service.getPaymentsForView();
    }
}
