package com.epam.esm.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record CreateOrderDetailDto(
        @Positive(message = "giftCertificateId can't be negative")
        @NotNull(message = "giftCertificateId can't be null")
        Long giftCertificateId,

        @Positive(message = "quantity can't be negative")
        @NotNull(message = "quantity can't be null")
        Integer quantity) {
}
