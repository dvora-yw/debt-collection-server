package com.debtcollection.service;

import com.debtcollection.dto.message.MessageCreateDto;
import com.debtcollection.dto.message.MessageDto;
import com.debtcollection.dto.message.MessageUpdateDto;
import com.debtcollection.dto.message.MessageViewDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.EndClient;
import com.debtcollection.entity.Message;
import com.debtcollection.entity.User;
import com.debtcollection.mapper.MessageMapper;
import com.debtcollection.repository.ClientRepository;
import com.debtcollection.repository.EndClientRepository;
import com.debtcollection.repository.MessageRepository;
import com.debtcollection.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final ClientRepository clientRepository;
    private final EndClientRepository endClientRepository;
    private final UserRepository UserRepository;
    private final MessageMapper mapper;

    public MessageService(
            MessageRepository messageRepository,
            ClientRepository clientRepository,
            EndClientRepository endClientRepository,
            UserRepository UserRepository,
            MessageMapper mapper
    ) {
        this.messageRepository = messageRepository;
        this.clientRepository = clientRepository;
        this.endClientRepository = endClientRepository;
        this.UserRepository = UserRepository;
        this.mapper = mapper;
    }

    // CREATE
    public MessageDto create(MessageCreateDto dto) {
        Message entity = mapper.toEntity(dto);

        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() ->
                            new RuntimeException("Client not found with id " + dto.getClientId())
                    );
            entity.setClient(client);
        }

        if (dto.getEndClientId() != null) {
            EndClient endClient = endClientRepository.findById(dto.getEndClientId())
                    .orElseThrow(() ->
                            new RuntimeException("EndClient not found with id " + dto.getEndClientId())
                    );
            entity.setEndClient(endClient);
        }

        if (dto.getUserId() != null) {
            User User = UserRepository.findById(dto.getUserId())
                    .orElseThrow(() ->
                            new RuntimeException("User not found with id " + dto.getUserId())
                    );
            entity.setUser(User);
        }

        return mapper.toResponseDto(messageRepository.save(entity));
    }

    // UPDATE
    public MessageDto update(Long id, MessageUpdateDto dto) {
        Message entity = messageRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Message not found with id " + id)
                );

        mapper.updateEntityFromDto(dto, entity);

        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() ->
                            new RuntimeException("Client not found with id " + dto.getClientId())
                    );
            entity.setClient(client);
        }

        if (dto.getEndClientId() != null) {
            EndClient endClient = endClientRepository.findById(dto.getEndClientId())
                    .orElseThrow(() ->
                            new RuntimeException("EndClient not found with id " + dto.getEndClientId())
                    );
            entity.setEndClient(endClient);
        }

        if (dto.getUserId() != null) {
            User User = UserRepository.findById(dto.getUserId())
                    .orElseThrow(() ->
                            new RuntimeException("User not found with id " + dto.getUserId())
                    );
            entity.setUser(User);
        }

        return mapper.toResponseDto(entity);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<MessageDto> getAll() {
        return messageRepository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public MessageDto getById(Long id) {
        return messageRepository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() ->
                        new RuntimeException("Message not found with id " + id)
                );
    }

    // DELETE
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }
    public List<MessageViewDto> getMessages() {
        return messageRepository.findAllForView();
    }
}
