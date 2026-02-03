package com.debtcollection.service;

import com.debtcollection.dto.paymentAllocation.PaymentAllocationCreateDto;
import com.debtcollection.dto.paymentAllocation.PaymentAllocationDto;
import com.debtcollection.dto.paymentAllocation.PaymentAllocationUpdateDto;
import com.debtcollection.entity.PaymentAllocation;
import com.debtcollection.mapper.PaymentAllocationMapper;
import com.debtcollection.repository.PaymentAllocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentAllocationService {

    private final PaymentAllocationRepository repository;
    private final PaymentAllocationMapper mapper;

    public PaymentAllocationService(PaymentAllocationRepository repository, PaymentAllocationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PaymentAllocationDto create(PaymentAllocationCreateDto dto) {
        PaymentAllocation e = mapper.toEntity(dto);
        return mapper.toDto(repository.save(e));
    }

    public PaymentAllocationDto getById(Long id) {
        PaymentAllocation e = repository.findById(id).orElseThrow(() -> new RuntimeException("PaymentAllocation not found"));
        return mapper.toDto(e);
    }

    public List<PaymentAllocationDto> findByPaymentId(Long paymentId) {
        return repository.findByPaymentId(paymentId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<PaymentAllocationDto> findByPaymentChargeId(Long paymentChargeId) {
        return repository.findByPaymentChargeId(paymentChargeId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public PaymentAllocationDto update(Long id, PaymentAllocationUpdateDto dto) {
        PaymentAllocation e = repository.findById(id).orElseThrow(() -> new RuntimeException("PaymentAllocation not found"));
        mapper.updateEntityFromDto(dto, e);
        return mapper.toDto(repository.save(e));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}