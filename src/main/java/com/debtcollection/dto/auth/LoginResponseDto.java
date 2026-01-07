package com.debtcollection.dto.auth;

import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class LoginResponseDto {
//    private String token;
    private UserDto user;
    private Boolean emailVerified;
    private String token;

}
