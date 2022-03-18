package com.epam.esm.security;

import com.epam.esm.security.filter.JwtAuthenticationFilter;
import com.epam.esm.security.filter.OktaJwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterChainConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OktaJwtAuthenticationFilter oktaJwtAuthenticationFilter;

    public SecurityFilterChainConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, OktaJwtAuthenticationFilter oktaJwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oktaJwtAuthenticationFilter = oktaJwtAuthenticationFilter;
    }

    @Override
    public void configure(HttpSecurity builder) {
        builder
                .addFilterBefore(oktaJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, OktaJwtAuthenticationFilter.class);
    }
}
