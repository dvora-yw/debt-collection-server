package com.debtcollection.dto.client;

import com.debtcollection.dto.contact.ClientContactDto;
import com.debtcollection.entity.EntityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ClientCreateDto {

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








}
