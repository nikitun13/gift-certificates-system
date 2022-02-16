package com.epam.esm.dao.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EntityScan("com.epam.esm.entity")
@ComponentScan(value = "com.epam.esm", lazyInit = true)
public class DaoTestConfig {
}
