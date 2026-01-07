package com.debtcollection.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class LoginRequestDto {
    private String email;
    private String password;
    private Boolean remember;

}
