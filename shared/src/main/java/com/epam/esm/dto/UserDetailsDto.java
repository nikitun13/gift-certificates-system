package com.epam.esm.dto;

import com.epam.esm.entity.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public record UserDetailsDto(Long id,
                             String email,
                             String password,
                             String firstName,
                             String lastName,
                             Role role) implements CustomUserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Long getId() {
        return id;
    }
}
