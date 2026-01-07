package com.debtcollection.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal() instanceof String principal && principal.equals("anonymousUser")) {
            throw new IllegalStateException("No authenticated user");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails customUser) {
            return customUser.getId();
        }

        throw new IllegalStateException("No authenticated user");
    }
}
