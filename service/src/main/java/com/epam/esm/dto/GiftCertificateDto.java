package com.epam.esm.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GiftCertificateDto(
        Long id,
        String name,
        String description,
        Long price,
        Integer duration,
        LocalDateTime createDate,
        LocalDateTime lastUpdateDate,
        List<TagDto> tags) {
}
