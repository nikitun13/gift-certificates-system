package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record OrderDto(Long id,
                       @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                       LocalDateTime createDate,
                       @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                       LocalDateTime lastUpdateDate,
                       Long totalPrice) {
}
