package com.debtcollection.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class VerifyEmailRequest {
    private String code;

}
