package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.Page;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final TagMapper tagMapper;

    public TagServiceImpl(TagDao tagDao, TagMapper tagMapper) {
        this.tagDao = tagDao;
        this.tagMapper = tagMapper;
    }

    @Override
    public List<TagDto> findAll(Page page) {
        return tagDao.findAll(page).stream()
                .map(tagMapper::toTagDto)
                .toList();
    }

    @Override
    public Optional<TagDto> findById(Long id) {
        return tagDao.findById(id)
                .map(tagMapper::toTagDto);
    }

    @Override
    public boolean delete(Long id) {
        return tagDao.delete(id);
    }

    @Override
    public TagDto create(CreateTagDto createTagDto) {
        Tag entity = tagMapper.toTag(createTagDto);
        tagDao.create(entity);
        return tagMapper.toTagDto(entity);
    }

    @Override
    public Optional<TagDto> findByName(String name) {
        return tagDao.findByName(name)
                .map(tagMapper::toTagDto);
    }
}
