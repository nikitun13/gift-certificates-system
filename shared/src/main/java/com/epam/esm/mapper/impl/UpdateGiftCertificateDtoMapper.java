package com.epam.esm.mapper.impl;

import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.Mapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class UpdateGiftCertificateDtoMapper implements Mapper<GiftCertificate, UpdateGiftCertificateDto> {

    private final Mapper<Tag, CreateTagDto> tagDtoMapper;

    public UpdateGiftCertificateDtoMapper(Mapper<Tag, CreateTagDto> tagDtoMapper) {
        this.tagDtoMapper = tagDtoMapper;
    }

    @Override
    public GiftCertificate mapToEntity(UpdateGiftCertificateDto dto) {
        var name = dto.name();
        var description = dto.description();
        var price = dto.price();
        var duration = dto.duration();
        var tagDtoList = dto.tags();
        List<Tag> tags = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(tagDtoList)) {
            tags = tagDtoList.stream()
                    .map(tagDtoMapper::mapToEntity)
                    .collect(toList());
        }

        return GiftCertificate.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(tags)
                .build();
    }

    @Override
    public UpdateGiftCertificateDto mapToDto(GiftCertificate entity) {
        List<CreateTagDto> tags = entity.getTags().stream()
                .map(tagDtoMapper::mapToDto)
                .toList();
        return new UpdateGiftCertificateDto(
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDuration(),
                tags
        );
    }
}
