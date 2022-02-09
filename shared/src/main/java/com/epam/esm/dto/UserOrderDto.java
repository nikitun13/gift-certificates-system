package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserOrderDto(Long id,
                           @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                           LocalDateTime createDate,
                           Long totalPrice) {
}
