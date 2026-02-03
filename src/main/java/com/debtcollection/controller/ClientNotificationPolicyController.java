package com.debtcollection.controller;

import com.debtcollection.dto.clientNotificationPolicy.ClientNotificationPolicyDto;
import com.debtcollection.service.ClientNotificationPolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients/{clientId}/notification-policy")
public class ClientNotificationPolicyController {

    private final ClientNotificationPolicyService service;

    public ClientNotificationPolicyController(ClientNotificationPolicyService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ClientNotificationPolicyDto> getPolicy(@PathVariable Long clientId) {
        return ResponseEntity.ok(service.getForClient(clientId));
    }

    @PostMapping
    public ResponseEntity<ClientNotificationPolicyDto> savePolicy(
            @PathVariable Long clientId,
            @RequestBody ClientNotificationPolicyDto dto) {
        ClientNotificationPolicyDto saved = service.saveForClient(clientId, dto);
        return ResponseEntity.ok(saved);
    }
}
