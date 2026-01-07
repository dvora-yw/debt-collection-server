package com.debtcollection.dto.user;

import com.debtcollection.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateDto {
    private String userName;
    private String password;
    private Role role;
    private String phone;
    private Long clientId;
    private Long endClientId;
    private String email;


}