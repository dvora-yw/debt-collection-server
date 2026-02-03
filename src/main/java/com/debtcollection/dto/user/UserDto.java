package com.debtcollection.dto.user;

import com.debtcollection.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private Role role;
    private String phone;
    private Boolean enabled;
    private String email;
    private Boolean emailVerified;
    private Long clientId;
    private Long endClientId;
    private String identificationNumber;
}
