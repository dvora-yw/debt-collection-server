package com.debtcollection.controller;

import com.debtcollection.dto.auth.LoginRequestDto;
import com.debtcollection.dto.client.ClientCreateDto;
import com.debtcollection.dto.client.ClientDto;
import com.debtcollection.dto.client.ClientUpdateDto;
import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.User;
import com.debtcollection.mapper.ClientMapper;
import com.debtcollection.security.CustomUserDetails;
import com.debtcollection.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor

public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;
    // =======================
    // יצירת לקוח חדש
    // =======================
    @PostMapping("/add")
    public ResponseEntity<ClientDto> create(@RequestBody ClientDto dto,Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        ClientDto createdClient = clientMapper.toResponseDto(
                clientService.createClient(dto, currentUserId)
        );

        return ResponseEntity.ok(createdClient);
    }


    // =======================
    // קבלת כל הלקוחות
    // =======================
    @GetMapping
    public List<ClientDto> getAll() {
        return clientService.getAllClients();
    }

    // =======================
    // קבלת לקוח לפי ID
    // =======================
    @GetMapping("/{clientId}")
    public ClientDto getById(@PathVariable Long clientId) {
        return clientService.getClientById(clientId);
    }

    // =======================
    // עדכון לקוח
    // =======================
    /*@PutMapping("/{clientId}")
    public ClientDto update(
            @PathVariable Long clientId,
            @RequestBody ClientDto dto
    ) {
        return clientService.updateClient(clientId, dto);
    }*/

    // =======================
    // מחיקת לקוח
    // =======================
    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
    }
}
