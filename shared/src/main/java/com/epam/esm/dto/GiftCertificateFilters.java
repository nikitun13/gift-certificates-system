package com.epam.esm.dto;

import java.util.List;

public record GiftCertificateFilters(
        List<String> tags,
        String name,
        String description,
        List<String> orderBy) {
}
