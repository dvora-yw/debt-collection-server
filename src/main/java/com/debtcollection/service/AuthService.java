package com.debtcollection.service;

import com.debtcollection.dto.auth.LoginRequestDto;
import com.debtcollection.dto.auth.LoginResponseDto;
import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.User;
import com.debtcollection.mapper.UserMapper;
import com.debtcollection.repository.UserRepository;
import com.debtcollection.security.CustomUserDetails;
import com.debtcollection.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;


    public LoginResponseDto login(LoginRequestDto dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid credentials");
//      String token = jwtService.generateToken(user);
        CustomUserDetails userDetails = new CustomUserDetails(
                user,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        user.setEmailVerified(false);

        String code = generateCode();
        user.setEmailVerificationCode(code);
        userRepository.save(user);
        emailService.sendOtp(user.getEmail(), code);



        return new LoginResponseDto(userMapper.toDto(user),Boolean.FALSE,null);
    }

    public LoginResponseDto verifyEmail(String code) {

        User user = userRepository.findByEmailVerificationCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid verification code"));

       // user.setEmailVerified(true);
        user.setEmailVerificationCode(null);
        userRepository.save(user);
        String token = jwtUtil.generateToken(user);

        return new LoginResponseDto(userMapper.toDto(user),Boolean.TRUE,token);
    }

    public void resendCode(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String code = generateCode();
        user.setEmailVerificationCode(code);

        userRepository.save(user);
        emailService.sendOtp(user.getEmail(), code);
    }

    private String generateCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}
