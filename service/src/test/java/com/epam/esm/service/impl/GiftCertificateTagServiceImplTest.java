package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagService;
import com.epam.esm.service.ServiceTestConfig;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ServiceTestConfig.class)
@ExtendWith(SpringExtension.class)
class GiftCertificateTagServiceImplTest {

    private final GiftCertificateTagService service;

    private final TagService tagService;
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateTagDao dao;

    private GiftCertificateDto kfcCertificateDto;
    private GiftCertificateDto oneHundredDollarsCertificateDto;
    private GiftCertificateDto cocaColaCertificateDto;

    private GiftCertificateDto kfcCertificateDtoWithTags;
    private GiftCertificateDto oneHundredDollarsCertificateDtoWithTags;
    private GiftCertificateDto cocaColaCertificateDtoWithTags;

    private final TagDto kfcTagDto = new TagDto(2L, "kfc");
    private final TagDto moneyCertificateTagDto = new TagDto(3L, "money certificate");

    @Autowired
    GiftCertificateTagServiceImplTest(TagService tagService,
                                      GiftCertificateService giftCertificateService,
                                      GiftCertificateTagDao dao) {
        this.tagService = tagService;
        this.giftCertificateService = giftCertificateService;
        this.dao = dao;

        service = new GiftCertificateTagServiceImpl(giftCertificateService, tagService, dao);
    }

    @BeforeEach
    void init() {
        GiftCertificate cocaColaCertificate = GiftCertificate.builder()
                .id(1L)
                .name("Free coca-cola certificate")
                .description("Simple use it and get coca-cola")
                .price(1000L)
                .duration(10)
                .createDate(LocalDateTime.parse("2022-01-01T12:00:00"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-02T13:30:25"))
                .build();

        GiftCertificate kfcCertificate = GiftCertificate.builder()
                .id(2L)
                .name("KFC certificate")
                .description("10 dollars certificate")
                .price(1000L)
                .duration(10)
                .createDate(LocalDateTime.parse("2022-01-03T16:30:20"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-03T16:30:20"))
                .build();

        GiftCertificate oneHundredDollarsCertificate = GiftCertificate.builder()
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

        cocaColaCertificateDtoWithTags = new GiftCertificateDto(
                cocaColaCertificate.getId(),
                cocaColaCertificate.getName(),
                cocaColaCertificate.getDescription(),
                cocaColaCertificate.getPrice(),
                cocaColaCertificate.getDuration(),
                cocaColaCertificate.getCreateDate(),
                cocaColaCertificate.getLastUpdateDate(),
                Collections.emptyList()
        );

        kfcCertificateDtoWithTags = new GiftCertificateDto(
                kfcCertificate.getId(),
                kfcCertificate.getName(),
                kfcCertificate.getDescription(),
                kfcCertificate.getPrice(),
                kfcCertificate.getDuration(),
                kfcCertificate.getCreateDate(),
                kfcCertificate.getLastUpdateDate(),
                List.of(kfcTagDto, moneyCertificateTagDto)
        );

        oneHundredDollarsCertificateDtoWithTags = new GiftCertificateDto(
                oneHundredDollarsCertificate.getId(),
                oneHundredDollarsCertificate.getName(),
                oneHundredDollarsCertificate.getDescription(),
                oneHundredDollarsCertificate.getPrice(),
                oneHundredDollarsCertificate.getDuration(),
                oneHundredDollarsCertificate.getCreateDate(),
                oneHundredDollarsCertificate.getLastUpdateDate(),
                List.of(moneyCertificateTagDto)
        );
    }

    @Test
    @Tag("findAll")
    void shouldFindAllCertificatesWithTags() {
        Mockito.doReturn(List.of(kfcTagDto, moneyCertificateTagDto))
                .when(tagService)
                .findByGiftCertificate(kfcCertificateDto);
        Mockito.doReturn(List.of(moneyCertificateTagDto))
                .when(tagService)
                .findByGiftCertificate(oneHundredDollarsCertificateDto);
        Mockito.doReturn(Collections.emptyList())
                .when(tagService)
                .findByGiftCertificate(cocaColaCertificateDto);
        Mockito.doReturn(List.of(kfcCertificateDto, cocaColaCertificateDto, oneHundredDollarsCertificateDto))
                .when(giftCertificateService)
                .findAll();
        List<GiftCertificateDto> expected = List.of(
                kfcCertificateDtoWithTags,
                cocaColaCertificateDtoWithTags,
                oneHundredDollarsCertificateDtoWithTags
        );

        List<GiftCertificateDto> actual = service.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findById")
    void shouldFindByIdAndSetTagsIfGivenIdExists() {
        Long existingId = oneHundredDollarsCertificateDto.id();
        Mockito.doReturn(Optional.of(oneHundredDollarsCertificateDto))
                .when(giftCertificateService)
                .findById(existingId);
        Mockito.doReturn(List.of(moneyCertificateTagDto))
                .when(tagService)
                .findByGiftCertificate(oneHundredDollarsCertificateDto);
        Optional<GiftCertificateDto> expected = Optional.of(oneHundredDollarsCertificateDtoWithTags);

        Optional<GiftCertificateDto> actual = service.findById(existingId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findById")
    void shouldReturnEmptyOptionalIfNoSuchId() {
        Long noSuchId = 505434L;
        Mockito.doReturn(Optional.empty())
                .when(giftCertificateService)
                .findById(noSuchId);

        Optional<GiftCertificateDto> actual = service.findById(noSuchId);

        assertThat(actual).isEmpty();
    }

    @Test
    @Tag("update")
    void shouldDelegateUpdateToCertificateServiceAndReturnFalse() {
        Long noSuchId = 54325L;
        String newDescription = "New description";
        UpdateGiftCertificateDto updateDto = new UpdateGiftCertificateDto(
                null, newDescription, null, null, null);
        Mockito.doReturn(Boolean.FALSE)
                .when(giftCertificateService)
                .update(updateDto, noSuchId);

        boolean actual = service.update(updateDto, noSuchId);

        assertThat(actual).isFalse();
    }

    @Test
    @Tag("update")
    void shouldUpdateOnlyCertificateIfTagsNull() {
        String newDescription = "New description";
        Long id = kfcCertificateDto.id();
        UpdateGiftCertificateDto updateDto = new UpdateGiftCertificateDto(
                null, newDescription, null, null, null);
        Mockito.doReturn(Boolean.TRUE)
                .when(giftCertificateService)
                .update(updateDto, id);

        boolean actual = service.update(updateDto, id);

        assertThat(actual).isTrue();
    }

    @Test
    @Tag("update")
    void shouldUpdateOnlyCertificateIfTagsEmpty() {
        String newDescription = "New description";
        Long id = kfcCertificateDto.id();
        UpdateGiftCertificateDto updateDto = new UpdateGiftCertificateDto(
                null, newDescription, null, null, Collections.emptyList());
        Mockito.doReturn(Boolean.TRUE)
                .when(giftCertificateService)
                .update(updateDto, id);

        boolean actual = service.update(updateDto, id);

        assertThat(actual).isTrue();
    }

    @Test
    @Tag("update")
    void shouldUpdateCertificateAndTagsIfTagsNotEmpty() {
        String newDescription = "New description";
        Long id = kfcCertificateDto.id();
        String newTagName = "new tag";
        CreateTagDto newTag = new CreateTagDto(newTagName);
        UpdateGiftCertificateDto updateDto = new UpdateGiftCertificateDto(
                null, newDescription, null, null, List.of(newTag));
        Mockito.doReturn(Boolean.TRUE)
                .when(giftCertificateService)
                .update(updateDto, id);
        Long newTagId = 3L;
        TagDto toBeReturnedFromDao = new TagDto(newTagId, newTagName);
        Mockito.doReturn(toBeReturnedFromDao)
                .when(tagService)
                .createIfNotExists(newTag);

        service.update(updateDto, id);

        Mockito.verify(dao)
                .create(id, newTagId);
    }

    @Test
    @Tag("create")
    void shouldOnlyDelegateCreationToCertificateServiceIfTagsEmpty() {
        UpdateGiftCertificateDto createDto = new UpdateGiftCertificateDto(
                oneHundredDollarsCertificateDto.name(),
                oneHundredDollarsCertificateDto.description(),
                oneHundredDollarsCertificateDto.price(),
                oneHundredDollarsCertificateDto.duration(),
                Collections.emptyList()
        );
        Mockito.doReturn(oneHundredDollarsCertificateDto)
                .when(giftCertificateService)
                .create(createDto);

        GiftCertificateDto actual = service.create(createDto);

        assertThat(actual).isEqualTo(oneHundredDollarsCertificateDto);
    }

    @Test
    @Tag("create")
    void shouldDelegateCreationToAndCreateRelationshipWithGivenTags() {
        CreateTagDto createTagDto = new CreateTagDto(moneyCertificateTagDto.name());
        UpdateGiftCertificateDto createDto = new UpdateGiftCertificateDto(
                oneHundredDollarsCertificateDto.name(),
                oneHundredDollarsCertificateDto.description(),
                oneHundredDollarsCertificateDto.price(),
                oneHundredDollarsCertificateDto.duration(),
                List.of(createTagDto)
        );
        Mockito.doReturn(oneHundredDollarsCertificateDto)
                .when(giftCertificateService)
                .create(createDto);
        Mockito.doReturn(moneyCertificateTagDto)
                .when(tagService)
                .createIfNotExists(createTagDto);

        service.create(createDto);

        Mockito.verify(dao)
                .create(oneHundredDollarsCertificateDto.id(), moneyCertificateTagDto.id());
    }

    @Test
    @Tag("create")
    void shouldCreateCertificateAndRelationshipWithTagsAndReturnCertificateWithTags() {
        CreateTagDto createTagDto = new CreateTagDto(moneyCertificateTagDto.name());
        UpdateGiftCertificateDto createDto = new UpdateGiftCertificateDto(
                oneHundredDollarsCertificateDto.name(),
                oneHundredDollarsCertificateDto.description(),
                oneHundredDollarsCertificateDto.price(),
                oneHundredDollarsCertificateDto.duration(),
                List.of(createTagDto)
        );
        Mockito.doReturn(oneHundredDollarsCertificateDto)
                .when(giftCertificateService)
                .create(createDto);
        Mockito.doReturn(moneyCertificateTagDto)
                .when(tagService)
                .createIfNotExists(createTagDto);

        GiftCertificateDto actual = service.create(createDto);

        assertThat(actual).isEqualTo(oneHundredDollarsCertificateDtoWithTags);
    }

    @Test
    @Tag("findGiftCertificatesByParams")
    void shouldFindAllIfEmptyParamsAndOrderByReceived() {
        Mockito.doReturn(List.of(kfcTagDto, moneyCertificateTagDto))
                .when(tagService)
                .findByGiftCertificate(kfcCertificateDto);
        Mockito.doReturn(List.of(moneyCertificateTagDto))
                .when(tagService)
                .findByGiftCertificate(oneHundredDollarsCertificateDto);
        Mockito.doReturn(Collections.emptyList())
                .when(tagService)
                .findByGiftCertificate(cocaColaCertificateDto);
        Mockito.doReturn(List.of(kfcCertificateDto, cocaColaCertificateDto, oneHundredDollarsCertificateDto))
                .when(giftCertificateService)
                .findAll();
        List<GiftCertificateDto> expected = List.of(
                kfcCertificateDtoWithTags,
                cocaColaCertificateDtoWithTags,
                oneHundredDollarsCertificateDtoWithTags
        );

        List<GiftCertificateDto> actual = service.findGiftCertificatesByParams(
                Collections.emptyMap(), Collections.emptyList());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findGiftCertificatesByParams")
    void shouldFindAllInGivenOrderIfEmptyParamsReceived() {
        List<String> orderBy = List.of("-name");
        Mockito.doReturn(List.of(kfcTagDto, moneyCertificateTagDto))
                .when(tagService)
                .findByGiftCertificate(kfcCertificateDto);
        Mockito.doReturn(List.of(moneyCertificateTagDto))
                .when(tagService)
                .findByGiftCertificate(oneHundredDollarsCertificateDto);
        Mockito.doReturn(Collections.emptyList())
                .when(tagService)
                .findByGiftCertificate(cocaColaCertificateDto);
        Mockito.doReturn(List.of(kfcCertificateDto, cocaColaCertificateDto, oneHundredDollarsCertificateDto))
                .when(giftCertificateService)
                .findByParams(Collections.emptyMap(), Collections.emptyMap(), orderBy);
        List<GiftCertificateDto> expected = List.of(
                kfcCertificateDtoWithTags,
                cocaColaCertificateDtoWithTags,
                oneHundredDollarsCertificateDtoWithTags
        );

        List<GiftCertificateDto> actual = service.findGiftCertificatesByParams(
                Collections.emptyMap(), orderBy);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findGiftCertificatesByParams")
    void shouldFilterUnknownTagParamsAndDelegateToCertificateServiceTheRest() {
        List<String> orderBy = Collections.emptyList();
        String dummyValue = "dummy";
        Map<String, String> params = Map.of(
                "tagName", dummyValue,
                "tag", dummyValue,
                "tag~", dummyValue,
                "tag ", dummyValue,
                "tagFDSf", dummyValue,
                "tagg", dummyValue,
                "tagg~", dummyValue,
                "tagPrice~", dummyValue,
                "description", dummyValue,
                "abcsdf", dummyValue
        );
        Map<String, String> tagParams = Map.of("name", dummyValue);

        Mockito.doReturn(Collections.emptyList())
                .when(tagService)
                .findByGiftCertificate(cocaColaCertificateDto);
        Mockito.doReturn(List.of(cocaColaCertificateDto))
                .when(giftCertificateService)
                .findByParams(params, tagParams, orderBy);
        List<GiftCertificateDto> expected = List.of(cocaColaCertificateDtoWithTags);

        List<GiftCertificateDto> actual = service.findGiftCertificatesByParams(params, orderBy);

        assertThat(actual).isEqualTo(expected);
    }
}
