package com.debtcollection.service;


import com.debtcollection.dto.clientNote.ClientNoteCreateDto;
import com.debtcollection.dto.clientNote.ClientNoteDto;

import com.debtcollection.dto.clientNote.ClientNoteUpdateDto;
import com.debtcollection.entity.ClientNote;
import com.debtcollection.mapper.ClientNoteMapper;
import com.debtcollection.repository.ClientNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientNoteService {

    private final ClientNoteRepository repository;
    private final ClientNoteMapper mapper;

    public ClientNoteDto create(ClientNoteCreateDto createDto) {
        ClientNote entity = mapper.toEntity(createDto);

        if (entity.getCreatedAt() == null) entity.setCreatedAt(LocalDateTime.now());
        ClientNote saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    public ClientNoteDto getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("ClientNote not found: " + id));
    }

    public List<ClientNoteDto> listAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<ClientNoteDto> listByClientId(Long clientId) {
        return repository.findByClientId(clientId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public ClientNoteDto update(Long id, ClientNoteUpdateDto updateDto) {
        ClientNote entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientNote not found: " + id));
        mapper.updateEntityFromDto(updateDto, entity);
        ClientNote saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("ClientNote not found: " + id);
        repository.deleteById(id);
    }
}