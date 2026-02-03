package com.debtcollection.controller;

import com.debtcollection.dto.auth.LoginRequestDto;
import com.debtcollection.dto.auth.LoginResponseDto;
import com.debtcollection.dto.auth.ResendCodeRequest;
import com.debtcollection.dto.auth.VerifyEmailRequest;
import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.User;
import com.debtcollection.mapper.UserMapper;
import com.debtcollection.service.AuthService;
import com.debtcollection.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto user = authService.login(dto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<LoginResponseDto> verifyEmail(@RequestBody VerifyEmailRequest req) {
        LoginResponseDto user = authService.verifyCode(req.getUserId(),req.getCode());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/resend-code")
    public ResponseEntity<Void> resendCode(
            @RequestBody ResendCodeRequest request) {

        authService.resendCode(request.getUserId());
        return ResponseEntity.ok().build();
    }

}



