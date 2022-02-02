package com.epam.esm.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan(lazyInit = true,
        excludeFilters = @ComponentScan.Filter(Configuration.class))
public class DaoTestConfig {

    @Bean
    @Primary
    public DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/ddl.sql")
                .addScript("classpath:sql/dml.sql")
                .build();
    }

    @Bean
    @Primary
    public JdbcTemplate testJdbcTemplate() {
        return new JdbcTemplate(testDataSource());
    }

    @Bean
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(testDataSource());
    }
}
