package com.epam.esm.dto;

public record OrderDetailDto(Long id,
                             GiftCertificateDto giftCertificate,
                             Long price,
                             Integer quantity) {
}
