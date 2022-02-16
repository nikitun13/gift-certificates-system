package com.epam.esm;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = "com.epam.esm.mapper", lazyInit = true)
public class MapperTestConfig {
}
