package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.Mapper;
import com.epam.esm.service.TagService;
import com.epam.esm.service.config.ServiceTestConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ServiceTestConfig.class)
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
                .findAll(new Page(1));

        List<TagDto> actual = service.findAll(new Page(1));

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
}
