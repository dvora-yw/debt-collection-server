package com.debtcollection.service;

import com.debtcollection.dto.debt.DebtCreateDto;
import com.debtcollection.dto.debt.DebtDto;
import com.debtcollection.dto.debt.DebtUpdateDto;
import com.debtcollection.entity.Debt;
import com.debtcollection.entity.EndClient;
import com.debtcollection.mapper.DebtMapper;
import com.debtcollection.repository.DebtRepository;
import com.debtcollection.repository.EndClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DebtService {

    private final DebtRepository debtRepository;
    private final EndClientRepository endClientRepository;
    private final DebtMapper mapper;

    public DebtService(
            DebtRepository debtRepository,
            EndClientRepository endClientRepository,
            DebtMapper mapper
    ) {
        this.debtRepository = debtRepository;
        this.endClientRepository = endClientRepository;
        this.mapper = mapper;
    }

    // CREATE
    public DebtDto create(DebtCreateDto dto) {
        Debt entity = mapper.toEntity(dto);

        EndClient endClient = endClientRepository.findById(dto.getEndClientId())
                .orElseThrow(() ->
                        new RuntimeException("EndClient not found with id " + dto.getEndClientId())
                );

        entity.setEndClient(endClient);

        return mapper.toResponseDto(debtRepository.save(entity));
    }

    // UPDATE
    public DebtDto update(Long id, DebtUpdateDto dto) {
        Debt entity = debtRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Debt not found with id " + id)
                );

        mapper.updateEntityFromDto(dto, entity);

        if (dto.getEndClientId() != null) {
            EndClient endClient = endClientRepository.findById(dto.getEndClientId())
                    .orElseThrow(() ->
                            new RuntimeException("EndClient not found with id " + dto.getEndClientId())
                    );
            entity.setEndClient(endClient);
        }

        return mapper.toResponseDto(entity);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<DebtDto> getAll() {
        return debtRepository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public DebtDto getById(Long id) {
        return debtRepository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() ->
                        new RuntimeException("Debt not found with id " + id)
                );
    }

    // DELETE
    public void delete(Long id) {
        debtRepository.deleteById(id);
    }
}
