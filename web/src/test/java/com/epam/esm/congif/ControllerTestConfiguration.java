package com.epam.esm.congif;

import com.epam.esm.util.JpaUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JpaUtil.class)
public class ControllerTestConfiguration {
}
