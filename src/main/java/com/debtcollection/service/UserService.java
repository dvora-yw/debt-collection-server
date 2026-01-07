package com.debtcollection.service;

import com.debtcollection.dto.user.UserCreateDto;
import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.User;
import com.debtcollection.mapper.UserMapper;
import com.debtcollection.repository.ClientRepository;
import com.debtcollection.repository.EndClientRepository;
import com.debtcollection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final EndClientRepository endClientRepository;

    public UserDto createUser(UserCreateDto dto) {
        User user = userMapper.toEntity(dto);

        // הצפנה
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getClientId() != null) {
            user.setClient(clientRepository.findById(dto.getClientId()).orElseThrow());
        }

        if (dto.getEndClientId() != null) {
            user.setEndClient(endClientRepository.findById(dto.getEndClientId()).orElseThrow());
        }

        return userMapper.toDto(userRepository.save(user));
    }
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + id)
                );
    }
}

