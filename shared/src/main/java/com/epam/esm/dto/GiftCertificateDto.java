package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record GiftCertificateDto(
        Long id,
        String name,
        String description,
        Long price,
        Integer duration,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createDate,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastUpdateDate,
        List<TagDto> tags) {
}
