package com.debtcollection.dto.auth;

import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private Long userId;
    private Long clientId;
    private Long endClientId; // when login by nationalId or email for end-client
    private String message;
    private Boolean emailVerified;
    private String token;

}
