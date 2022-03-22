package com.epam.esm.security;

import com.epam.esm.dto.CreateOktaUserDto;
import com.epam.esm.dto.OktaUserDetailsDto;
import com.epam.esm.dto.UserDetailsDto;
import com.epam.esm.service.UserAuthenticationService;
import com.epam.esm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;

public class CustomOpaqueTokenAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomOpaqueTokenAuthenticationProvider.class);

    private static final String EMAIL = "sub";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";

    private final OpaqueTokenAuthenticationProvider delegate;
    private final UserService userService;
    private final UserAuthenticationService userAuthenticationService;

    public CustomOpaqueTokenAuthenticationProvider(OpaqueTokenAuthenticationProvider delegate,
                                                   UserService userService,
                                                   UserAuthenticationService userAuthenticationService) {
        this.delegate = delegate;
        this.userService = userService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) delegate.authenticate(authentication)
                .getPrincipal();
        UserDetails userDetails = loadUserOrElseCreate(principal);
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }

    private UserDetails loadUserOrElseCreate(OAuth2AuthenticatedPrincipal principal) {
        String email = principal.getName();
        UserDetailsDto userDetailsDto;
        try {
            userDetailsDto = (UserDetailsDto) userService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            log.trace("Creating new account for new okta user with email: {} ", email);
            CreateOktaUserDto oktaUser = convert(principal);
            userDetailsDto = userAuthenticationService.signUp(oktaUser);
        }
        return new OktaUserDetailsDto(userDetailsDto, principal.getAuthorities());
    }

    private CreateOktaUserDto convert(OAuth2AuthenticatedPrincipal principal) {
        String email = principal.getAttribute(EMAIL);
        String firstName = principal.getAttribute(FIRST_NAME);
        String lastName = principal.getAttribute(LAST_NAME);
        return new CreateOktaUserDto(email, firstName, lastName);
    }
}
