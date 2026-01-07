package com.debtcollection.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientContactDto {
    private Long id;
    private Long clientId;
    public String firstName;
    public String lastName;
    public String role;
    public String phone;
    public String email;
}
