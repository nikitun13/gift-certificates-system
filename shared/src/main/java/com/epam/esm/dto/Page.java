package com.epam.esm.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

public record Page(
        @Positive(message = "page can't be negative or 0")
        int page,

        @Positive(message = "page size can't be negative or 0")
        @Max(value = 100, message = "page size can't be more than 100")
        int pageSize) {

    private static final int DEFAULT_PAGE_SIZE = 10;

    public Page(int page) {
        this(page, DEFAULT_PAGE_SIZE);
    }

    public int getOffset() {
        return pageSize * (page - 1);
    }
}
