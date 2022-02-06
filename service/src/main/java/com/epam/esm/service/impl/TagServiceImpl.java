package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.Page;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.Mapper;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final Mapper<Tag, TagDto> tagDtoMapper;
    private final Mapper<Tag, CreateTagDto> createTagDtoMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao,
                          Mapper<Tag, TagDto> tagDtoMapper,
                          Mapper<Tag, CreateTagDto> createTagDtoMapper) {
        this.tagDao = tagDao;
        this.tagDtoMapper = tagDtoMapper;
        this.createTagDtoMapper = createTagDtoMapper;
    }

    @Override
    public List<TagDto> findAll(Page page) {
        return tagDao.findAll(page)
                .stream()
                .map(tagDtoMapper::mapToDto)
                .toList();
    }

    @Override
    public Optional<TagDto> findById(Long id) {
        return tagDao.findById(id)
                .map(tagDtoMapper::mapToDto);
    }

    @Override
    public boolean delete(Long id) {
        return tagDao.delete(id);
    }

    @Override
    public TagDto create(CreateTagDto createTagDto) {
        Tag entity = createTagDtoMapper.mapToEntity(createTagDto);
        tagDao.create(entity);
        return tagDtoMapper.mapToDto(entity);
    }

    @Override
    public Optional<TagDto> findByName(String name) {
        return tagDao.findByName(name)
                .map(tagDtoMapper::mapToDto);
    }
}
