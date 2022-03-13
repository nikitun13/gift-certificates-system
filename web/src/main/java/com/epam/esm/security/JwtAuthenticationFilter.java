package com.epam.esm.security;

import com.epam.esm.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        extractAuthBearerHeader(request)
                .ifPresent(jwt -> authenticate(jwt, request));
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractAuthBearerHeader(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        return StringUtils.isNotEmpty(authorization)
                && authorization.startsWith(BEARER_PREFIX)
                ? Optional.of(authorization.substring(BEARER_PREFIX.length()))
                : Optional.empty();
    }

    private void authenticate(String jwt, HttpServletRequest request) {
        try {
            Claims claims = jwtUtil.decodeJwt(jwt);
            String subject = claims.getSubject();

            UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            WebAuthenticationDetails details = new WebAuthenticationDetailsSource()
                    .buildDetails(request);
            authentication.setDetails(details);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            log.warn("Invalid jwt token", e);
        }
    }
}
