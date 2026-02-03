package com.debtcollection.service;

import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsCreateDto;
import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsDto;
import com.debtcollection.dto.recurringPaymentDetails.RecurringPaymentDetailsUpdateDto;
import com.debtcollection.entity.RecurringPaymentDetails;
import com.debtcollection.mapper.RecurringPaymentDetailsMapper;
import com.debtcollection.repository.RecurringPaymentDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecurringPaymentDetailsService {

    private final RecurringPaymentDetailsRepository repository;
    private final RecurringPaymentDetailsMapper mapper;

    public RecurringPaymentDetailsService(RecurringPaymentDetailsRepository repository,
                                          RecurringPaymentDetailsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public RecurringPaymentDetailsDto create(RecurringPaymentDetailsCreateDto dto) {
        RecurringPaymentDetails e = mapper.toEntity(dto);
        return mapper.toDto(repository.save(e));
    }

    public RecurringPaymentDetailsDto getById(Long id) {
        RecurringPaymentDetails e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RecurringPaymentDetails not found"));
        return mapper.toDto(e);
    }

    public List<RecurringPaymentDetailsDto> findByEndClientId(Long endClientId) {
        return repository.findByEndClientId(endClientId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public RecurringPaymentDetailsDto update(Long id, RecurringPaymentDetailsUpdateDto dto) {
        RecurringPaymentDetails e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RecurringPaymentDetails not found"));
        mapper.updateEntityFromDto(dto, e);
        return mapper.toDto(repository.save(e));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}