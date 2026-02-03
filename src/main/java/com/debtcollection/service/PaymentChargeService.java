package com.debtcollection.service;
import com.debtcollection.dto.paymentCharge.PaymentChargeCreateDto;
import com.debtcollection.dto.paymentCharge.PaymentChargeDto;
import com.debtcollection.dto.paymentCharge.PaymentChargeUpdateDto;
import com.debtcollection.entity.PaymentCharge;
import com.debtcollection.mapper.PaymentChargeMapper;
import com.debtcollection.repository.PaymentChargeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentChargeService {

    private final PaymentChargeRepository repository;
    private final PaymentChargeMapper mapper;

    public PaymentChargeService(PaymentChargeRepository repository, PaymentChargeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PaymentChargeDto create(PaymentChargeCreateDto dto) {
        PaymentCharge e = mapper.toEntity(dto);
        return mapper.toDto(repository.save(e));
    }

    public PaymentChargeDto getById(Long id) {
        PaymentCharge e = repository.findById(id).orElseThrow(() -> new RuntimeException("PaymentCharge not found"));
        return mapper.toDto(e);
    }

    public List<PaymentChargeDto> findByRecurringPaymentId(Long recurringPaymentId) {
        return repository.findByRecurringPaymentId(recurringPaymentId).stream().map(mapper::toDto).collect(Collectors.toList());
    }
    public List<PaymentChargeDto> findByEndClientIdAndStatus(Long endClientId, String status) {
        return repository.findByEndClient_IdAndStatus(endClientId, status)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public PaymentChargeDto update(Long id, PaymentChargeUpdateDto dto) {
        PaymentCharge e = repository.findById(id).orElseThrow(() -> new RuntimeException("PaymentCharge not found"));
        mapper.updateEntityFromDto(dto, e);
        return mapper.toDto(repository.save(e));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}