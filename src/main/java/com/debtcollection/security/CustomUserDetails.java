package com.debtcollection.security;

import com.debtcollection.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@Data
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Long clientId;

    private final Collection<? extends GrantedAuthority> authorities;
    private final Boolean enabled;

    public CustomUserDetails(User user,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = user.getId();
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.clientId = user.getClient() != null ? user.getClient().getId() : null; // <-- כאן

        this.authorities = authorities;
        this.enabled = user.getEnabled();

    }



    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}
