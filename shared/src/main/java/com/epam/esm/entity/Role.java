package com.epam.esm.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CLIENT,
    ADMIN;

    private static final String ROLE_PREFIX = "ROLE_";

    private final String authority = ROLE_PREFIX + name();

    @Override
    public String getAuthority() {
        return authority;
    }
}
