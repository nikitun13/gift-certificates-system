package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.Mapper;
import com.epam.esm.service.ServiceTestConfig;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ServiceTestConfig.class)
@ExtendWith(SpringExtension.class)
class TagServiceImplTest {

    private final TagDao dao;
    private final TagService service;

    private final Tag cocaColaTag = new Tag(1L, "coca-cola");
    private final Tag kfcTag = new Tag(2L, "kfc");
    private final Tag moneyCertificateTag = new Tag(3L, "money certificate");

    private final TagDto cocaColaTagDto = new TagDto(1L, "coca-cola");
    private final TagDto kfcTagDto = new TagDto(2L, "kfc");
    private final TagDto moneyCertificateTagDto = new TagDto(3L, "money certificate");

    @Autowired
    TagServiceImplTest(TagDao dao,
                       Mapper<Tag, TagDto> tagDtoMapper,
                       Mapper<Tag, CreateTagDto> createTagDtoMapper) {
        this.dao = dao;
        service = new TagServiceImpl(dao, tagDtoMapper, createTagDtoMapper);
    }

    @Test
    @org.junit.jupiter.api.Tag("findAll")
    void shouldReturnAllEntitiesMappedToTagDto() {
        List<Tag> daoReturn = List.of(cocaColaTag, kfcTag, moneyCertificateTag);
        List<TagDto> expected = List.of(cocaColaTagDto, kfcTagDto, moneyCertificateTagDto);
        Mockito.doReturn(daoReturn)
                .when(dao)
                .findAll();

        List<TagDto> actual = service.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("findById")
    void shouldFindEntityByIdAndMapItToTagDto() {
        Long id = kfcTag.getId();
        Mockito.doReturn(Optional.of(kfcTag))
                .when(dao)
                .findById(id);
        Optional<TagDto> expected = Optional.of(kfcTagDto);

        Optional<TagDto> actual = service.findById(id);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("delete")
    void shouldDelegateToDaoAndReturnTrue() {
        Long existingId = kfcTag.getId();
        Mockito.doReturn(Boolean.TRUE)
                .when(dao)
                .delete(existingId);

        boolean actual = service.delete(existingId);

        assertThat(actual).isTrue();
    }

    @Test
    @org.junit.jupiter.api.Tag("delete")
    void shouldDelegateToDaoAndReturnFalse() {
        Long noSuchId = 5000L;
        Mockito.doReturn(Boolean.FALSE)
                .when(dao)
                .delete(noSuchId);

        boolean actual = service.delete(noSuchId);

        assertThat(actual).isFalse();
    }

    @Test
    @org.junit.jupiter.api.Tag("create")
    void shouldCreateNewTag() {
        String newTagName = "new tag";
        CreateTagDto newTag = new CreateTagDto(newTagName);
        Long newId = 4L;
        Mockito.doAnswer(invocation -> {
                    Tag tag = invocation.getArgument(0, Tag.class);
                    tag.setId(newId);
                    return null;
                })
                .when(dao)
                .create(Tag.builder().name(newTagName).build());
        TagDto expected = new TagDto(newId, newTagName);

        TagDto actual = service.create(newTag);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("createIfNotExists")
    void shouldCreateNewTagIfNotExists() {
        String newTagName = "new tag 2";
        CreateTagDto newTag = new CreateTagDto(newTagName);
        Long newId = 5L;
        Mockito.doAnswer(invocation -> {
                    Tag tag = invocation.getArgument(0, Tag.class);
                    tag.setId(newId);
                    return null;
                })
                .when(dao)
                .create(Tag.builder().name(newTagName).build());
        Mockito.doReturn(Optional.empty())
                .when(dao)
                .findByName(newTagName);
        TagDto expected = new TagDto(newId, newTagName);

        TagDto actual = service.createIfNotExists(newTag);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("createIfNotExists")
    void shouldNotCreateNewTagIfAlreadyExists() {
        String oldTagName = cocaColaTag.getName();
        CreateTagDto newTag = new CreateTagDto(oldTagName);
        Mockito.doReturn(Optional.of(cocaColaTag))
                .when(dao)
                .findByName(oldTagName);

        TagDto actual = service.createIfNotExists(newTag);

        assertThat(actual).isEqualTo(cocaColaTagDto);
    }

    @Test
    @org.junit.jupiter.api.Tag("findByName")
    void shouldFindExistingTagByNameAndMapToTagDto() {
        String tagName = moneyCertificateTag.getName();
        Mockito.doReturn(Optional.of(moneyCertificateTag))
                .when(dao)
                .findByName(tagName);

        Optional<TagDto> actual = service.findByName(tagName);

        assertThat(actual).isEqualTo(Optional.of(moneyCertificateTagDto));
    }

    @Test
    @org.junit.jupiter.api.Tag("findByName")
    void shouldReturnEmptyOptionalIfNoSuchTagName() {
        String tagName = "no such tag name";
        Mockito.doReturn(Optional.empty())
                .when(dao)
                .findByName(tagName);

        Optional<TagDto> actual = service.findByName(tagName);

        assertThat(actual).isEmpty();
    }

    @Test
    @org.junit.jupiter.api.Tag("findByGiftCertificate")
    void shouldFindTagsByGiftCertificateAndMapToTagDto() {
        Long certificateId = 3L;
        GiftCertificateDto oneHundredDollarsCertificateDto = new GiftCertificateDto(
                certificateId,
                "100 dollars certificate",
                "Buy anything for 100 dollars",
                10000L,
                20,
                LocalDateTime.parse("2022-01-05T14:29:12"),
                LocalDateTime.parse("2022-01-05T14:29:12"),
                new ArrayList<>()
        );
        Mockito.doReturn(List.of(moneyCertificateTag, kfcTag))
                .when(dao)
                .findByGiftCertificateId(certificateId);
        List<TagDto> expected = List.of(moneyCertificateTagDto, kfcTagDto);

        List<TagDto> actual = service.findByGiftCertificate(oneHundredDollarsCertificateDto);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("findByGiftCertificate")
    void shouldReturnEmptyListIfNoTagsForGivenCertificate() {
        Long certificateId = 4L;
        GiftCertificateDto certificateWithoutTags = new GiftCertificateDto(
                certificateId,
                "No tags",
                "Buy it",
                222L,
                22,
                LocalDateTime.parse("2022-01-04T19:59:14"),
                LocalDateTime.parse("2022-01-07T15:21:14"),
                new ArrayList<>()
        );
        Mockito.doReturn(Collections.emptyList())
                .when(dao)
                .findByGiftCertificateId(certificateId);

        List<TagDto> actual = service.findByGiftCertificate(certificateWithoutTags);

        assertThat(actual).isEmpty();
    }
}
