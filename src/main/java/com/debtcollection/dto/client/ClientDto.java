package com.debtcollection.dto.client;

import com.debtcollection.dto.contact.ClientContactDto;
import com.debtcollection.dto.endClient.EndClientDto;
import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.EndClient;
import com.debtcollection.entity.EntityType;
import com.debtcollection.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private Long id;

    @NotBlank
    private String name;
    @NotNull
    private EntityType entityType;
    @NotBlank
    private String identificationNumber;
    private String address;
    private String phone;
    private String email;
    private String fax;
    private String notes;
    private LocalDate establishedDate;
    private String vatNumber;
    private String paymentModel;
    private String paymentTerms;
    private List<ClientContactDto> contacts;
    private Set<EndClientDto> endClients;
    private List<UserDto> users;



}
