package com.epam.esm.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class Page {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Positive(message = "page can't be negative or 0")
    private int page;

    @Positive(message = "page size can't be negative or 0")
    @Max(value = 100, message = "page size can't be more than 100")
    private int pageSize;

    public Page(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public Page(int page) {
        this(page, DEFAULT_PAGE_SIZE);
    }

    public Page() {
        this(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    public int getOffset() {
        return pageSize * (page - 1);
    }

    public int page() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int pageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page1 = (Page) o;
        return page == page1.page && pageSize == page1.pageSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, pageSize);
    }

    @Override
    public String toString() {
        return "Page{"
                + "page=" + page
                + ", pageSize=" + pageSize
                + '}';
    }
}
