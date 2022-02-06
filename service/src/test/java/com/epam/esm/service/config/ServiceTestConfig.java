package com.epam.esm.service.config;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = "com.epam.esm.mapper")
public class ServiceTestConfig {

    @Bean
    public GiftCertificateDao giftCertificateDao() {
        return Mockito.mock(GiftCertificateDao.class);
    }

    @Bean
    public TagDao tagDao() {
        return Mockito.mock(TagDao.class);
    }

    @Bean
    public GiftCertificateService giftCertificateService() {
        return Mockito.mock(GiftCertificateService.class);
    }

    @Bean
    public TagService tagService() {
        return Mockito.mock(TagService.class);
    }
}
