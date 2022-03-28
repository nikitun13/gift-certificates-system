package com.epam.esm.security;

import com.epam.esm.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationProvider(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken bearer = (BearerTokenAuthenticationToken) authentication;
        String token = bearer.getToken();
        try {
            String subject = jwtUtil.decodeJwt(token).getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            result.setDetails(bearer.getDetails());
            return result;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("jwt is invalid", e);
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
