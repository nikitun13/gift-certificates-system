package com.epam.esm.mapper.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class TagDtoMapper implements Mapper<Tag, TagDto> {

    @Override
    public Tag mapToEntity(TagDto dto) {
        var id = dto.id();
        var name = dto.name();

        return Tag.builder()
                .id(id)
                .name(name)
                .build();
    }

    @Override
    public TagDto mapToDto(Tag entity) {
        return new TagDto(
                entity.getId(),
                entity.getName()
        );
    }
}
