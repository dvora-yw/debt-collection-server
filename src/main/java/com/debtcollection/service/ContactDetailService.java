package com.debtcollection.service;

import com.debtcollection.dto.contactDetail.ContactDetailCreateDto;
import com.debtcollection.dto.contactDetail.ContactDetailDto;
import com.debtcollection.dto.contactDetail.ContactDetailUpdateDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.ContactDetail;
import com.debtcollection.entity.EndClient;
import com.debtcollection.entity.Person;
import com.debtcollection.mapper.ContactDetailMapper;
import com.debtcollection.repository.ClientRepository;
import com.debtcollection.repository.ContactDetailRepository;
import com.debtcollection.repository.EndClientRepository;
import com.debtcollection.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContactDetailService {

    private final ContactDetailRepository contactDetailRepository;
    private final ClientRepository clientRepository;
    private final EndClientRepository endClientRepository;
    private final PersonRepository personRepository;
    private final ContactDetailMapper mapper;

    public ContactDetailService(
            ContactDetailRepository contactDetailRepository,
            ClientRepository clientRepository,
            EndClientRepository endClientRepository,
            PersonRepository personRepository,
            ContactDetailMapper mapper
    ) {
        this.contactDetailRepository = contactDetailRepository;
        this.clientRepository = clientRepository;
        this.endClientRepository = endClientRepository;
        this.personRepository = personRepository;
        this.mapper = mapper;
    }

    // CREATE
    public ContactDetailDto create(ContactDetailCreateDto dto) {
        validateExactlyOneOwner(dto.getClientId(),dto.getEndClientId(),dto.getPersonId());
        ContactDetail entity = mapper.toEntity(dto);

        setRelations(entity, dto.getClientId(), dto.getEndClientId(), dto.getPersonId());

        return mapper.toResponseDto(contactDetailRepository.save(entity));
    }

    // UPDATE
    public ContactDetailDto update(Long id, ContactDetailUpdateDto dto) {
        ContactDetail entity = contactDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ContactDetail not found with id " + id));

        mapper.updateEntityFromDto(dto, entity);
        validateExactlyOneOwner(dto.getClientId(),dto.getEndClientId(),dto.getPersonId());

        setRelations(entity, dto.getClientId(), dto.getEndClientId(), dto.getPersonId());

        return mapper.toResponseDto(entity);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<ContactDetailDto> getAll() {
        return contactDetailRepository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }



    // GET BY ID
    @Transactional(readOnly = true)
    public ContactDetailDto getById(Long id) {
        return contactDetailRepository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("ContactDetail not found with id " + id));
    }

    // DELETE
    public void delete(Long id) {
        contactDetailRepository.deleteById(id);
    }

    // ===== helper =====
    private void setRelations(
            ContactDetail entity,
            Long clientId,
            Long endClientId,
            Long personId
    ) {
        if (clientId != null) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found with id " + clientId));
            entity.setClient(client);
        } else {
            entity.setClient(null);
        }

        if (endClientId != null) {
            EndClient endClient = endClientRepository.findById(endClientId)
                    .orElseThrow(() -> new RuntimeException("EndClient not found with id " + endClientId));
            entity.setEndClient(endClient);
        } else {
            entity.setEndClient(null);
        }

        if (personId != null) {
            Person person = personRepository.findById(personId)
                    .orElseThrow(() -> new RuntimeException("Person not found with id " + personId));
            entity.setPerson(person);
        } else {
            entity.setPerson(null);
        }
    }
    private void validateExactlyOneOwner( Long clientId,Long endClientId,Long personId) {
        int owners = 0;

        if (clientId != null) owners++;
        if (endClientId != null) owners++;
        if (personId != null) owners++;

        if (owners != 1) {
            throw new IllegalArgumentException(
                    "ContactDetail must belong to exactly one owner: client, endClient or person"
            );
        }
    }
}
