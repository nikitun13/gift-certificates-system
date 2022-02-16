package com.epam.esm.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.springframework.hateoas.PagedModel.PageMetadata;

class PaginationUtilTest {

    private final PaginationUtil paginationUtil;

    PaginationUtilTest() {
        paginationUtil = new PaginationUtil();
    }

    @Test
    @Tag("buildPageMetadata")
    void buildPageMetadata() {
        PageImpl<?> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(5, 200), 1932);
        PageMetadata expected = new PageMetadata(200, 5, 1932, 10);

        PageMetadata actual = paginationUtil.buildPageMetadata(page);

        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
