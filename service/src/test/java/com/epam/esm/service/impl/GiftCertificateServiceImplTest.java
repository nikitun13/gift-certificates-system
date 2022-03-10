package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateFilters;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mapper.GiftCertificateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @Mock
    private GiftCertificateDao dao;

    @Mock
    private GiftCertificateMapper mapper;

    private GiftCertificate cocaColaCertificate;
    private GiftCertificate kfcCertificate;
    private GiftCertificate oneHundredDollarsCertificate;

    private GiftCertificateDto cocaColaCertificateDto;
    private GiftCertificateDto kfcCertificateDto;
    private GiftCertificateDto oneHundredDollarsCertificateDto;

    @BeforeEach
    void init() {
        cocaColaCertificate = GiftCertificate.builder()
                .id(1L)
                .name("Free coca-cola certificate")
                .description("Simple use it and get coca-cola")
                .price(1000L)
                .duration(10)
                .createDate(LocalDateTime.parse("2022-01-01T12:00:00"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-02T13:30:25"))
                .build();

        kfcCertificate = GiftCertificate.builder()
                .id(2L)
                .name("KFC certificate")
                .description("10 dollars certificate")
                .price(1000L)
                .duration(10)
                .createDate(LocalDateTime.parse("2022-01-03T16:30:20"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-03T16:30:20"))
                .build();

        oneHundredDollarsCertificate = GiftCertificate.builder()
                .id(3L)
                .name("100 dollars certificate")
                .description("Buy anything for 100 dollars")
                .price(10000L)
                .duration(20)
                .createDate(LocalDateTime.parse("2022-01-05T14:29:12"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-05T14:29:12"))
                .build();

        cocaColaCertificateDto = new GiftCertificateDto(
                cocaColaCertificate.getId(),
                cocaColaCertificate.getName(),
                cocaColaCertificate.getDescription(),
                cocaColaCertificate.getPrice(),
                cocaColaCertificate.getDuration(),
                cocaColaCertificate.getCreateDate(),
                cocaColaCertificate.getLastUpdateDate(),
                new ArrayList<>()
        );

        kfcCertificateDto = new GiftCertificateDto(
                kfcCertificate.getId(),
                kfcCertificate.getName(),
                kfcCertificate.getDescription(),
                kfcCertificate.getPrice(),
                kfcCertificate.getDuration(),
                kfcCertificate.getCreateDate(),
                kfcCertificate.getLastUpdateDate(),
                new ArrayList<>()
        );

        oneHundredDollarsCertificateDto = new GiftCertificateDto(
                oneHundredDollarsCertificate.getId(),
                oneHundredDollarsCertificate.getName(),
                oneHundredDollarsCertificate.getDescription(),
                oneHundredDollarsCertificate.getPrice(),
                oneHundredDollarsCertificate.getDuration(),
                oneHundredDollarsCertificate.getCreateDate(),
                oneHundredDollarsCertificate.getLastUpdateDate(),
                new ArrayList<>()
        );
    }

    @Test
    @Tag("findAll")
    void shouldDelegateToDaoAndResultToMapper() {
        GiftCertificateFilters filters = new GiftCertificateFilters(
                List.of("extreme", "rest"), "cerf", "adc", List.of("-name", "createDate"));
        Pageable pageable = PageRequest.of(0, 20);
        doReturn(cocaColaCertificateDto)
                .when(mapper)
                .toGiftCertificateDto(cocaColaCertificate);
        doReturn(kfcCertificateDto)
                .when(mapper)
                .toGiftCertificateDto(kfcCertificate);
        doReturn(oneHundredDollarsCertificateDto)
                .when(mapper)
                .toGiftCertificateDto(oneHundredDollarsCertificate);
        List<GiftCertificate> list = List.of(cocaColaCertificate, kfcCertificate, oneHundredDollarsCertificate);
        Page<GiftCertificate> page = new PageImpl<>(list, pageable, list.size());
        doReturn(page)
                .when(dao)
                .findAll(filters, pageable);
        List<GiftCertificateDto> content = List.of(cocaColaCertificateDto, kfcCertificateDto, oneHundredDollarsCertificateDto);
        Page<GiftCertificateDto> expected = new PageImpl<>(content, pageable, content.size());

        Page<GiftCertificateDto> actual = service.findAll(filters, pageable);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findById")
    void shouldDelegateFindByIdToDaoAndResultToMapper() {
        Long id = kfcCertificate.getId();
        doReturn(Optional.of(kfcCertificate))
                .when(dao)
                .findById(id);
        doReturn(kfcCertificateDto)
                .when(mapper)
                .toGiftCertificateDto(kfcCertificate);
        Optional<GiftCertificateDto> expected = Optional.of(kfcCertificateDto);

        Optional<GiftCertificateDto> actual = service.findById(id);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("update")
    void shouldDelegateToMapperThenSetIdAndResultDelegateToDaoUpdate() {
        Long id = oneHundredDollarsCertificate.getId();
        String newName = "new name";
        Long newPrice = 555L;
        Integer newDuration = 11;
        UpdateGiftCertificateDto dto = new UpdateGiftCertificateDto(newName, null, newPrice, newDuration, null);
        GiftCertificate certificateWithoutId = GiftCertificate.builder()
                .name(newName)
                .price(newPrice)
                .duration(newDuration)
                .build();
        doReturn(certificateWithoutId)
                .when(mapper)
                .toGiftCertificate(dto);
        GiftCertificate certificate = GiftCertificate.builder()
                .id(id)
                .name(newName)
                .price(newPrice)
                .duration(newDuration)
                .build();
        doReturn(true)
                .when(dao)
                .update(certificate);

        boolean actual = service.update(dto, id);

        assertThat(actual).isTrue();
    }

    @Test
    @Tag("delete")
    void shouldDelegateDeleteToDao() {
        Long id = cocaColaCertificate.getId();
        doReturn(true)
                .when(dao)
                .delete(id);

        boolean actual = service.delete(id);

        assertThat(actual).isTrue();
    }

    @Test
    @Tag("create")
    void shouldDelegateToMapperThanCreateToDaoAndToMapperAgain() {
        String name = "new name";
        Long price = 555L;
        Integer duration = 11;
        String description = "description";
        UpdateGiftCertificateDto dto = new UpdateGiftCertificateDto(name, description, price, duration, Collections.emptyList());
        GiftCertificate certificate = GiftCertificate.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
        doReturn(certificate)
                .when(mapper)
                .toGiftCertificate(dto);
        Long id = 10L;
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate fullCertificate = GiftCertificate.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .createDate(now)
                .lastUpdateDate(now)
                .duration(duration)
                .build();
        doAnswer(invocation -> {
            GiftCertificate arg = invocation.getArgument(0);
            arg.setId(id);
            arg.setCreateDate(now);
            arg.setLastUpdateDate(now);
            return arg;
        })
                .when(dao)
                .create(certificate);
        GiftCertificateDto expected = new GiftCertificateDto(id, name, description, price, duration, now, now, Collections.emptyList());
        doReturn(expected)
                .when(mapper)
                .toGiftCertificateDto(fullCertificate);

        GiftCertificateDto actual = service.create(dto);

        assertThat(actual).isEqualTo(expected);
    }
}
