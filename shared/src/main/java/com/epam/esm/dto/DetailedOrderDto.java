package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record DetailedOrderDto(Long id,
                               @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                               LocalDateTime createDate,
                               @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                               LocalDateTime lastUpdateDate,
                               Long totalPrice,
                               List<OrderDetailDto> details) {
}
