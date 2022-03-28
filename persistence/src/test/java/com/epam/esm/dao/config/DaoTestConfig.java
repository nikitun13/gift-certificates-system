package com.epam.esm.dao.config;

import com.epam.esm.util.JpaUtil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@EntityScan("com.epam.esm.entity")
@ComponentScan(value = "com.epam.esm.dao")
@Import(JpaUtil.class)
public class DaoTestConfig {
}
