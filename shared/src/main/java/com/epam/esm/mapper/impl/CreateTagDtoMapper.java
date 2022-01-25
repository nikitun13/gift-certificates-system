package com.epam.esm.mapper.impl;

import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class CreateTagDtoMapper implements Mapper<Tag, CreateTagDto> {

    @Override
    public Tag mapToEntity(CreateTagDto dto) {
        var name = dto.name();

        return Tag.builder()
                .name(name)
                .build();
    }

    @Override
    public CreateTagDto mapToDto(Tag entity) {
        var name = entity.getName();
        return new CreateTagDto(name);
    }
}
