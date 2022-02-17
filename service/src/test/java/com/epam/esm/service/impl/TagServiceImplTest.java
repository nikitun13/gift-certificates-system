package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.TagMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl service;

    @Mock
    private TagDao dao;

    @Mock
    private TagMapper mapper;

    private final Tag cocaColaTag = new Tag(1L, "coca-cola");
    private final Tag kfcTag = new Tag(2L, "kfc");
    private final Tag moneyCertificateTag = new Tag(3L, "money certificate");

    private final TagDto cocaColaTagDto = new TagDto(1L, "coca-cola");
    private final TagDto kfcTagDto = new TagDto(2L, "kfc");
    private final TagDto moneyCertificateTagDto = new TagDto(3L, "money certificate");

    @Test
    @org.junit.jupiter.api.Tag("findAll")
    void shouldDelegateToDaoFindAllAndResultToMapper() {
        Pageable pageable = PageRequest.of(0, 20);
        List<Tag> tags = List.of(cocaColaTag, kfcTag, moneyCertificateTag);
        Page<Tag> page = new PageImpl<>(tags, pageable, tags.size());
        List<TagDto> tagDtoList = List.of(cocaColaTagDto, kfcTagDto, moneyCertificateTagDto);
        Page<TagDto> expected = new PageImpl<>(tagDtoList, pageable, tagDtoList.size());
        doReturn(cocaColaTagDto)
                .when(mapper)
                .toTagDto(cocaColaTag);
        doReturn(kfcTagDto)
                .when(mapper)
                .toTagDto(kfcTag);
        doReturn(moneyCertificateTagDto)
                .when(mapper)
                .toTagDto(moneyCertificateTag);
        doReturn(page)
                .when(dao)
                .findAll(pageable);

        Page<TagDto> actual = service.findAll(pageable);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("findById")
    void shouldDelegateFindByIdToDaoAndResultToMapper() {
        Long id = kfcTag.getId();
        doReturn(Optional.of(kfcTag))
                .when(dao)
                .findById(id);
        doReturn(kfcTagDto)
                .when(mapper)
                .toTagDto(kfcTag);
        Optional<TagDto> expected = Optional.of(kfcTagDto);

        Optional<TagDto> actual = service.findById(id);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("delete")
    void shouldDelegateDeleteToDao() {
        Long id = kfcTag.getId();
        doReturn(true)
                .when(dao)
                .delete(id);

        boolean actual = service.delete(id);

        assertThat(actual).isTrue();
    }

    @Test
    @org.junit.jupiter.api.Tag("create")
    void shouldDelegateToMapperThanCreateToDaoAndToMapperAgain() {
        String name = moneyCertificateTag.getName();
        Long id = moneyCertificateTag.getId();
        CreateTagDto createTagDto = new CreateTagDto(name);
        Tag tag = new Tag(null, name);
        doReturn(tag)
                .when(mapper)
                .toTag(createTagDto);
        doAnswer(invocation -> {
            Tag arg = invocation.getArgument(0);
            arg.setId(id);
            return arg;
        })
                .when(dao)
                .create(tag);
        doReturn(moneyCertificateTagDto)
                .when(mapper)
                .toTagDto(moneyCertificateTag);

        TagDto actual = service.create(createTagDto);

        assertThat(actual).isEqualTo(moneyCertificateTagDto);
    }

    @Test
    @org.junit.jupiter.api.Tag("findByName")
    void shouldDelegateFindByNameToDaoAndMappingToMapper() {
        String name = cocaColaTag.getName();
        doReturn(Optional.of(cocaColaTag))
                .when(dao)
                .findByName(name);
        doReturn(cocaColaTagDto)
                .when(mapper)
                .toTagDto(cocaColaTag);
        Optional<TagDto> expected = Optional.of(cocaColaTagDto);

        Optional<TagDto> actual = service.findByName(name);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("findTopTagOfUserWithTheHighestCostOfAllOrders")
    void shouldDelegateFindTopTagToDaoAndMappingToMapper() {
        doReturn(Optional.of(moneyCertificateTag))
                .when(dao)
                .findTopTagOfUserWithTheHighestCostOfAllOrders();
        doReturn(moneyCertificateTagDto)
                .when(mapper)
                .toTagDto(moneyCertificateTag);
        Optional<TagDto> expected = Optional.of(moneyCertificateTagDto);

        Optional<TagDto> actual = service.findTopTagOfUserWithTheHighestCostOfAllOrders();

        assertThat(actual).isEqualTo(expected);
    }
}
