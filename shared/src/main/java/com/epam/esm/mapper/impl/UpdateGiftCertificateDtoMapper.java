package com.epam.esm.mapper.impl;

import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UpdateGiftCertificateDtoMapper implements Mapper<GiftCertificate, UpdateGiftCertificateDto> {

    @Override
    public GiftCertificate mapToEntity(UpdateGiftCertificateDto dto) {
        var name = dto.name();
        var description = dto.description();
        var price = dto.price();
        var duration = dto.duration();

        return GiftCertificate.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
    }

    @Override
    public UpdateGiftCertificateDto mapToDto(GiftCertificate entity) {
        return new UpdateGiftCertificateDto(
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDuration(),
                new ArrayList<>()
        );
    }
}
