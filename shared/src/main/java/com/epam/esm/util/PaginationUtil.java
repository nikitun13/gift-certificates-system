package com.epam.esm.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.PagedModel.PageMetadata;

@Component
public class PaginationUtil {

    /**
     * Extracts page metadata from the {@link Page}
     * and creates {@link PageMetadata}.
     *
     * @param page page to extract page metadata.
     * @return metadata of the current pagination.
     */
    public PageMetadata buildPageMetadata(Page<?> page) {
        int number = page.getNumber();
        int size = page.getSize();
        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();

        return new PageMetadata(size, number, totalElements, totalPages);
    }
}
