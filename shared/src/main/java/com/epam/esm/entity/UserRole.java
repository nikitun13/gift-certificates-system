package com.epam.esm.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    CLIENT,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
