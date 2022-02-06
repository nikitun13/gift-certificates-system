package com.epam.esm.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

public record Page(@Positive int page,
                   @Positive @Max(100) int pageSize) {

    private static final int DEFAULT_PAGE_SIZE = 10;

    public Page(int page) {
        this(page, DEFAULT_PAGE_SIZE);
    }

    public int getOffset() {
        return pageSize * (page - 1);
    }
}
