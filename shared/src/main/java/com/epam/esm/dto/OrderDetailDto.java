package com.epam.esm.dto;

public record OrderDetailDto(Long id,
                             Long price,
                             Integer quantity,
                             GiftCertificateDtoWithoutTags giftCertificate) {
}
