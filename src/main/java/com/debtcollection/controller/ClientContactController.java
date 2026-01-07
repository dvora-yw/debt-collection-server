package com.debtcollection.controller;

import com.debtcollection.dto.contact.ClientContactDto;
import com.debtcollection.dto.endClient.EndClientDto;
import com.debtcollection.service.ClientContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client-contacts")
@RequiredArgsConstructor
public class ClientContactController {

    private final ClientContactService contactService;

    @PostMapping("/clients/{clientId}/contacts")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientContactDto create(
            @PathVariable Long clientId,
            @RequestBody ClientContactDto dto
    ) {
        return contactService.create(clientId, dto);
    }

    @PutMapping("/{contactId}")
    public ClientContactDto update(
            @PathVariable Long contactId,
            @RequestBody ClientContactDto dto
    ) {
        return contactService.update(contactId, dto);
    }
    @GetMapping("/contacts/{clientId}")
    public List<ClientContactDto> getByClient(@PathVariable Long clientId) {
        return contactService.getByClientId(clientId);
    }
    @DeleteMapping("/{contactId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long contactId) {
        contactService.delete(contactId);
    }


}

