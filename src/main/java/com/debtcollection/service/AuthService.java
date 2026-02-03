package com.debtcollection.service;

import com.debtcollection.dto.auth.LoginRequestDto;
import com.debtcollection.dto.auth.LoginResponseDto;
import com.debtcollection.entity.EndClient;
import com.debtcollection.entity.User;
import com.debtcollection.mapper.UserMapper;
import com.debtcollection.repository.EndClientRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final EndClientRepository endClientRepository;



    public LoginResponseDto login(LoginRequestDto dto) {
        // 1️⃣ Admin – username + password
        if (hasText(dto.getUsername())) {
            return loginAdmin(dto);
        }
        // 2️⃣ Client – email
        if (hasText(dto.getEmail())) {
            return loginByEmail(dto.getEmail());
        }
        // 3️⃣ EndClient – nationalId
        if (hasText(dto.getNationalId())) {
            return loginByNationalId(dto.getNationalId());
        }

        throw new RuntimeException("No valid login data provided");
    }

    /* =========================
       VERIFY CODE – STEP 2
       ========================= */
    public LoginResponseDto verifyCode(Long userId, String code) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEmailVerificationCode() == null ||
                !user.getEmailVerificationCode().equals(code)) {
            throw new RuntimeException("Invalid verification code");
        }

        user.setEmailVerified(true);
        user.setEmailVerificationCode(null);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        return new LoginResponseDto(
                user.getId(),
                user.getClient() != null ? user.getClient().getId() : null,
                user.getEndClient() != null ? user.getEndClient().getId() : null,
                "Login successful",
                true,
                token
        );
    }

    /* =========================
       PRIVATE FLOWS
       ========================= */

    private LoginResponseDto loginAdmin(LoginRequestDto dto) {

        User user = userRepository.findByUserName(dto.getUsername().trim())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return sendVerification(user);
    }

    private LoginResponseDto loginByEmail(String email) {

        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return sendVerification(user);
    }

    private LoginResponseDto loginByNationalId(String nationalId) {
        User user = userRepository.findByIdentificationNumber(nationalId.trim())
                .orElseThrow(() -> new RuntimeException("No user with email for end-client"));
        EndClient endClient = endClientRepository.findById(user.getEndClient().getId())
                .orElseThrow(() -> new RuntimeException("EndClient not found"));

//        User user = endClient.getUsers().stream()
//                .filter(u -> u.getEmail() != null)
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("No user with email for end-client"));

        return sendVerification(user);
    }

    /* =========================
       SEND OTP
       ========================= */
    private LoginResponseDto sendVerification(User user) {

        String code = generateCode();

        user.setEmailVerified(false);
        user.setEmailVerificationCode(code);
        userRepository.save(user);

        emailService.sendOtp(user.getEmail(), code);

        return new LoginResponseDto(
                user.getId(),
                user.getClient() != null ? user.getClient().getId() : null,
                user.getEndClient() != null ? user.getEndClient().getId() : null,
                "Verification code sent",
                false,
                null
        );
    }
    public void resendCode(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String code = generateCode();

        user.setEmailVerified(false);
        user.setEmailVerificationCode(code);
        userRepository.save(user);

        emailService.sendOtp(user.getEmail(), code);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
    private String generateCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}
