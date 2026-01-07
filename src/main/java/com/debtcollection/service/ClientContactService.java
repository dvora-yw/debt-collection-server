package com.debtcollection.service;

import com.debtcollection.dto.contact.ClientContactDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.ClientContacts;
import com.debtcollection.mapper.ClientContactMapper;
import com.debtcollection.repository.ClientContactRepository;
import com.debtcollection.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientContactService {

    private final ClientContactRepository contactRepository;
    private final ClientRepository clientRepository;
    private final ClientContactMapper mapper;

    public ClientContactDto create(Long clientId, ClientContactDto dto) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        ClientContacts contact = mapper.toEntity(dto);
        contact.setClient(client);

        contactRepository.save(contact);
        return mapper.toResponseDto(contact);
    }

    public ClientContactDto update(Long contactId, ClientContactDto dto) {
        ClientContacts contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));

        mapper.updateEntityFromDto(dto, contact);
        return mapper.toResponseDto(contact);
    }

    @Transactional(readOnly = true)
    public List<ClientContactDto> getByClientId(Long clientId) {
        return contactRepository.findByClientId(clientId)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    public void delete(Long contactId) {
        if (!contactRepository.existsById(contactId)) {
            throw new EntityNotFoundException("Contact not found");
        }
        contactRepository.deleteById(contactId);
    }
}

