package com.epam.esm.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DaoConfig {

    @Value("${db.driverClassName}")
    private String driverClassName;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.initPoolSize}")
    private int initPoolSize;
    @Value("${db.maxIdle}")
    private int maxIdle;
    @Value("${db.minIdle}")
    private int minIdle;
    @Value("${db.maxTotal}")
    private int maxTotal;

    @Bean(destroyMethod = "close")
    public BasicDataSource basicDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(driverClassName);
        basicDataSource.setUrl(url);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        basicDataSource.setInitialSize(initPoolSize);
        basicDataSource.setMaxIdle(maxIdle);
        basicDataSource.setMinIdle(minIdle);
        basicDataSource.setMaxTotal(maxTotal);
        return basicDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(basicDataSource());
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(basicDataSource());
    }
}
