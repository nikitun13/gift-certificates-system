package com.epam.esm.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public record OktaUserDetailsDto(Long id,
                                 UserDetailsDto userDetails,
                                 Collection<? extends GrantedAuthority> scopes) implements CustomUserDetails {

    public OktaUserDetailsDto(UserDetailsDto userDetails, List<String> scopes) {
        this(userDetails.id(),
                userDetails,
                scopes.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return scopes;
    }

    @Override
    public String getPassword() {
        return userDetails.getPassword();
    }

    @Override
    public String getUsername() {
        return userDetails.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userDetails.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userDetails.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userDetails.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userDetails.isEnabled();
    }

    @Override
    public Long getId() {
        return id;
    }
}
