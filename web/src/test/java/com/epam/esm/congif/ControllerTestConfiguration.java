package com.epam.esm.congif;

import com.epam.esm.service.UserAuthenticationService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.epam.esm.util")
public class ControllerTestConfiguration {

    @Bean
    public UserAuthenticationService userAuthenticationService() {
        return Mockito.mock(UserAuthenticationService.class);
    }
}
