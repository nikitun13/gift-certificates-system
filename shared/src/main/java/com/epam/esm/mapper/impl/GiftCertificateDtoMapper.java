package com.epam.esm.mapper.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GiftCertificateDtoMapper implements Mapper<GiftCertificate, GiftCertificateDto> {

    @Override
    public GiftCertificate mapToEntity(GiftCertificateDto dto) {
        var id = dto.id();
        var name = dto.name();
        var description = dto.description();
        var price = dto.price();
        var duration = dto.duration();
        var createDate = dto.createDate();
        var lastUpdateDate = dto.lastUpdateDate();

        return GiftCertificate.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();
    }

    @Override
    public GiftCertificateDto mapToDto(GiftCertificate entity) {
        return new GiftCertificateDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDuration(),
                entity.getCreateDate(),
                entity.getLastUpdateDate(),
                new ArrayList<>()
        );
    }
}
