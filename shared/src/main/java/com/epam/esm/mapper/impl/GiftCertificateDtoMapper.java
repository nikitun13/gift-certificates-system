package com.epam.esm.mapper.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.Mapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class GiftCertificateDtoMapper implements Mapper<GiftCertificate, GiftCertificateDto> {

    private final Mapper<Tag, TagDto> tagDtoMapper;

    public GiftCertificateDtoMapper(Mapper<Tag, TagDto> tagDtoMapper) {
        this.tagDtoMapper = tagDtoMapper;
    }

    @Override
    public GiftCertificate mapToEntity(GiftCertificateDto dto) {
        var id = dto.id();
        var name = dto.name();
        var description = dto.description();
        var price = dto.price();
        var duration = dto.duration();
        var createDate = dto.createDate();
        var lastUpdateDate = dto.lastUpdateDate();
        var tagDtoList = dto.tags();
        List<Tag> tags = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(tagDtoList)) {
            tags = tagDtoList.stream()
                    .map(tagDtoMapper::mapToEntity)
                    .collect(toList());
        }

        return GiftCertificate.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .tags(tags)
                .build();
    }

    @Override
    public GiftCertificateDto mapToDto(GiftCertificate entity) {
        List<TagDto> tags = entity.getTags().stream()
                .map(tagDtoMapper::mapToDto)
                .toList();
        return new GiftCertificateDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDuration(),
                entity.getCreateDate(),
                entity.getLastUpdateDate(),
                tags
        );
    }
}
