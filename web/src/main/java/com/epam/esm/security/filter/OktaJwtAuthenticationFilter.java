package com.epam.esm.security.filter;

import com.epam.esm.dto.OktaUserDetailsDto;
import com.epam.esm.dto.OktaUserDto;
import com.epam.esm.dto.UserDetailsDto;
import com.epam.esm.service.UserAuthenticationService;
import com.epam.esm.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class OktaJwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(OktaJwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TOKEN_TYPE_HINT_KEY = "token_type_hint";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String TOKEN_KEY = "token";

    private final RestTemplate template;
    private final UserService userService;
    private final UserAuthenticationService userAuthenticationService;
    private final String introspectUrl;
    private final HttpHeaders headers;

    public OktaJwtAuthenticationFilter(UserService userService,
                                       UserAuthenticationService userAuthenticationService,
                                       @Value("${okta.introspect.url}") String introspectUrl,
                                       @Value("${okta.client.id}") String clientId,
                                       @Value("${okta.client.secret}") String clientSecret) {
        this.userService = userService;
        this.introspectUrl = introspectUrl;
        this.userAuthenticationService = userAuthenticationService;
        this.template = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            extractAuthBearerHeader(request)
                    .map(this::parseJwt)
                    .ifPresent(user -> authenticate(user, request));
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractAuthBearerHeader(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        return StringUtils.isNotEmpty(authorization)
                && authorization.startsWith(BEARER_PREFIX)
                ? Optional.of(authorization.substring(BEARER_PREFIX.length()))
                : Optional.empty();
    }

    private void authenticate(OktaUserDto oktaUser, HttpServletRequest request) {
        if (oktaUser.isActive()) {
            UserDetails userDetails = loadUserOrElseCreate(oktaUser);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
            authentication.setDetails(details);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private UserDetails loadUserOrElseCreate(OktaUserDto oktaUser) {
        List<String> scopes = Arrays.stream(oktaUser.scope().split(" "))
                .toList();
        UserDetailsDto userDetailsDto;
        try {
            userDetailsDto = (UserDetailsDto) userService.loadUserByUsername(oktaUser.email());
        } catch (UsernameNotFoundException e) {
            log.info("Creating new account for new okta user with email: {} ", oktaUser.email());
            userDetailsDto = userAuthenticationService.signUp(oktaUser);
        }
        return new OktaUserDetailsDto(userDetailsDto, scopes);
    }

    private OktaUserDto parseJwt(String jwt) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(2);
        body.add(TOKEN_KEY, jwt);
        body.add(TOKEN_TYPE_HINT_KEY, ACCESS_TOKEN);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            return template.postForEntity(introspectUrl, request, OktaUserDto.class)
                    .getBody();
        } catch (RestClientException e) {
            log.warn("rest client exception occurred:", e);
            return new OktaUserDto(false, null, null, null, null);
        }
    }
}
