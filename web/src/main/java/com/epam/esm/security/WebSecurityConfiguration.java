package com.epam.esm.security;

import com.epam.esm.service.UserAuthenticationService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.JwtUtil;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public WebSecurityConfiguration(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .authenticationProvider(jwtAuthenticationProvider(null))
                .authenticationProvider(customOpaqueTokenAuthenticationProvider(null, null))
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() {
        return authenticationManagerBeanWithRuntimeException();
    }

    private AuthenticationManager authenticationManagerBeanWithRuntimeException() {
        try {
            return super.authenticationManagerBean();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint()))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(opaque -> opaque
                                .introspector(opaqueTokenIntrospector(null))
                                .authenticationManager(authenticationManagerBean()))
                );
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2ResourceServerProperties resourceServerProperties) {
        OAuth2ResourceServerProperties.Opaquetoken opaqueTokenProperties = resourceServerProperties.getOpaquetoken();
        String clientId = opaqueTokenProperties.getClientId();
        String clientSecret = opaqueTokenProperties.getClientSecret();
        String introspectionUri = opaqueTokenProperties.getIntrospectionUri();
        return new SpringOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
    }

    @Bean
    public CustomOpaqueTokenAuthenticationProvider customOpaqueTokenAuthenticationProvider(
            OpaqueTokenIntrospector introspector, UserAuthenticationService userAuthenticationService) {
        OpaqueTokenAuthenticationProvider delegate = new OpaqueTokenAuthenticationProvider(introspector);
        return new CustomOpaqueTokenAuthenticationProvider(delegate, userService, userAuthenticationService);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(JwtUtil jwtUtil) {
        return new JwtAuthenticationProvider(jwtUtil, userService);
    }
}
