package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Page;
import com.epam.esm.mapper.Mapper;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.config.ServiceTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ServiceTestConfig.class)
class GiftCertificateServiceImplTest {

    private final GiftCertificateDao dao;
    private final GiftCertificateService service;

    private GiftCertificate cocaColaCertificate;
    private GiftCertificate kfcCertificate;
    private GiftCertificate oneHundredDollarsCertificate;

    private GiftCertificateDto cocaColaCertificateDto;
    private GiftCertificateDto kfcCertificateDto;
    private GiftCertificateDto oneHundredDollarsCertificateDto;

    @Autowired
    GiftCertificateServiceImplTest(GiftCertificateDao dao,
                                   Mapper<GiftCertificate, GiftCertificateDto> giftCertificateDtoMapper,
                                   Mapper<GiftCertificate, UpdateGiftCertificateDto> updateGiftCertificateDtoMapper) {
        this.dao = dao;
        service = new GiftCertificateServiceImpl(dao, giftCertificateDtoMapper, updateGiftCertificateDtoMapper);
    }

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
    void shouldFindAllEntitiesAndMapThemToDtos() {
        List<GiftCertificate> daoReturn = List.of(cocaColaCertificate, kfcCertificate, oneHundredDollarsCertificate);
        Mockito.doReturn(daoReturn)
                .when(dao)
                .findAll(new Page(1));
        List<GiftCertificateDto> expected = List.of(
                cocaColaCertificateDto,
                kfcCertificateDto,
                oneHundredDollarsCertificateDto);

        List<GiftCertificateDto> actual = service.findAll(new Page(1));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findById")
    void shouldFindEntityByIdAndMapItToDto() {
        Mockito.doReturn(Optional.of(oneHundredDollarsCertificate))
                .when(dao)
                .findById(oneHundredDollarsCertificate.getId());
        Optional<GiftCertificateDto> expected = Optional.of(oneHundredDollarsCertificateDto);

        Optional<GiftCertificateDto> actual = service.findById(oneHundredDollarsCertificate.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findById")
    void shouldReturnEmptyOptionalIfNoSuchEntityWithTheGivenId() {
        Long noSuchId = 10000L;
        Mockito.doReturn(Optional.empty())
                .when(dao)
                .findById(noSuchId);
        Optional<GiftCertificateDto> expected = Optional.empty();

        Optional<GiftCertificateDto> actual = service.findById(noSuchId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("create")
    void shouldReturnCreatedEntityWithId() {
        UpdateGiftCertificateDto createNewCocaColaCertificateDto = new UpdateGiftCertificateDto(
                cocaColaCertificateDto.name(),
                cocaColaCertificateDto.description(),
                cocaColaCertificateDto.price(),
                cocaColaCertificateDto.duration(),
                new ArrayList<>());
        cocaColaCertificate.setId(null);
        Mockito.doAnswer(invocation -> {
                    GiftCertificate certificate = invocation.getArgument(0, GiftCertificate.class);
                    certificate.setId(cocaColaCertificateDto.id());
                    return null;
                })
                .when(dao)
                .create(Mockito.any());

        GiftCertificateDto actual = service.create(createNewCocaColaCertificateDto);

        assertThat(actual.id()).isEqualTo(cocaColaCertificateDto.id());
        assertThat(actual.createDate()).isNotNull();
        assertThat(actual.lastUpdateDate()).isNotNull();
    }

    @Test
    @Tag("delete")
    void shouldDelegateToDaoAndReturnTrue() {
        Long id = cocaColaCertificate.getId();
        Mockito.doReturn(Boolean.TRUE)
                .when(dao)
                .delete(id);

        boolean actual = service.delete(id);

        assertThat(actual).isTrue();
    }

    @Test
    @Tag("delete")
    void shouldDelegateToDaoAndReturnFalse() {
        Long id = 50000L;
        Mockito.doReturn(Boolean.FALSE)
                .when(dao)
                .delete(id);

        boolean actual = service.delete(id);

        assertThat(actual).isFalse();
    }

    @Test
    void shouldMapToEntityAndDelegateToDto() {
        Long id = kfcCertificateDto.id();
        String newDescription = "New description";
        UpdateGiftCertificateDto updateKfcCertificate = new UpdateGiftCertificateDto(
                null, newDescription, null, null, null);
        Mockito.doReturn(Boolean.TRUE)
                .when(dao)
                .update(Mockito.argThat(argument -> argument.getId().equals(id)
                        && argument.getDescription().equals(newDescription)
                        && argument.getLastUpdateDate() != null));

        boolean actual = service.update(updateKfcCertificate, id);

        assertThat(actual).isTrue();
    }
}
