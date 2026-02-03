package com.debtcollection.service;

import com.debtcollection.dto.accountBalance.AccountBalanceCreateDto;
import com.debtcollection.dto.accountBalance.AccountBalanceDto;
import com.debtcollection.dto.accountBalance.AccountBalanceUpdateDto;
import com.debtcollection.entity.AccountBalance;
import com.debtcollection.mapper.AccountBalanceMapper;
import com.debtcollection.repository.AccountBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountBalanceService {

    private final AccountBalanceRepository repository;
    private final AccountBalanceMapper mapper;

    public AccountBalanceDto create(AccountBalanceCreateDto dto) {
        AccountBalance e = mapper.toEntity(dto);
        return mapper.toDto(repository.save(e));
    }

    public AccountBalanceDto getById(Long id) {
        AccountBalance e = repository.findById(id).orElseThrow(() -> new RuntimeException("AccountBalance not found"));
        return mapper.toDto(e);
    }

    public Optional<AccountBalanceDto> findByEndClientId(Long endClientId) {
        return repository.findByEndClientId(endClientId).map(mapper::toDto);
    }

    public List<AccountBalanceDto> findAllByEndClientId(Long endClientId) {
        return repository.findAllByEndClientId(endClientId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public AccountBalanceDto update(Long id, AccountBalanceUpdateDto dto) {
        AccountBalance e = repository.findById(id).orElseThrow(() -> new RuntimeException("AccountBalance not found"));
        mapper.updateEntityFromDto(dto, e);
        return mapper.toDto(repository.save(e));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}