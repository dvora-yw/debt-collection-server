package com.debtcollection.service;

import com.debtcollection.dto.person.PersonCreateDto;
import com.debtcollection.dto.person.PersonDto;
import com.debtcollection.dto.person.PersonUpdateDto;
import com.debtcollection.entity.EndClient;
import com.debtcollection.entity.Person;
import com.debtcollection.mapper.PersonMapper;
import com.debtcollection.repository.EndClientRepository;
import com.debtcollection.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final EndClientRepository endClientRepository;
    private final PersonMapper mapper;

    public PersonService(
            PersonRepository personRepository,
            EndClientRepository endClientRepository,
            PersonMapper mapper
    ) {
        this.personRepository = personRepository;
        this.endClientRepository = endClientRepository;
        this.mapper = mapper;
    }

    // CREATE
    @Transactional
    public PersonDto create(PersonCreateDto dto) {
        Person person = mapper.toEntity(dto);

        if (dto.getEndClientId() != null) {
            EndClient endClient = endClientRepository.findById(dto.getEndClientId())
                    .orElseThrow(() ->
                            new RuntimeException("EndClient not found with id " + dto.getEndClientId())
                    );
            person.setEndClient(endClient);
        }

        Person saved = personRepository.save(person);
        return mapper.toResponseDto(saved);
    }

    // UPDATE
    @Transactional
    public PersonDto update(Long id, PersonUpdateDto dto) {
        Person person = personRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Person not found with id " + id)
                );

        mapper.updateEntityFromDto(dto, person);

        if (dto.getEndClientId() != null) {
            EndClient endClient = endClientRepository.findById(dto.getEndClientId())
                    .orElseThrow(() ->
                            new RuntimeException("EndClient not found with id " + dto.getEndClientId())
                    );
            person.setEndClient(endClient);
        }

        return mapper.toResponseDto(person);
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public PersonDto getById(Long id) {
        return personRepository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() ->
                        new RuntimeException("Person not found with id " + id)
                );
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<PersonDto> getAll() {
        return personRepository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<PersonDto> getAllByID(Long endClientId) {
       return personRepository.findByEndClientId(endClientId)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }
    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Person not found with id " + id);
        }
        personRepository.deleteById(id);
    }
}
