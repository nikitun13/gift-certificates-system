package com.epam.esm.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JpaUtilTest {

    private final JpaUtil jpaUtil = new JpaUtil();

    public static Stream<Arguments> nullDataForCreateOrder() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<?> path = mock(Path.class);
        String str = "dummy";
        return Stream.of(
                Arguments.of(null, null, null),
                Arguments.of(null, null, str),
                Arguments.of(cb, null, null),
                Arguments.of(null, path, null),
                Arguments.of(null, path, str),
                Arguments.of(cb, null, str),
                Arguments.of(cb, path, null)
        );
    }

    @Test
    @Tag("wrapWithPercentages")
    void shouldWrapStringWithPercentages() {
        String toBeWrapped = "dummy";
        String expected = "%dummy%";

        String actual = jpaUtil.wrapWithPercentages(toBeWrapped);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @Tag("wrapWithPercentages")
    void shouldThrowExceptionIfGivenStringForWrappingIsEmpty(String str) {
        assertThatThrownBy(() -> jpaUtil.wrapWithPercentages(str))
                .isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class);
    }

    @Test
    @Tag("createOrder")
    void shouldCreateAscendingOrder() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<?> path = mock(Path.class);
        String orderField = "name";

        jpaUtil.createOrder(cb, path, orderField);

        verify(path).get(orderField);
        verify(cb).asc(any());
    }

    @Test
    @Tag("createOrder")
    void shouldCreateDescendingOrder() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<?> path = mock(Path.class);
        String fieldNameWithOrderSign = "-name";
        String fieldName = "name";

        jpaUtil.createOrder(cb, path, fieldNameWithOrderSign);

        verify(path).get(fieldName);
        verify(cb).desc(any());
    }

    @ParameterizedTest
    @MethodSource("nullDataForCreateOrder")
    @Tag("createOrder")
    void shouldThrowNullPointerIfAnyOfArgsIsNull(CriteriaBuilder cb, Path<?> path, String orderField) {
        assertThatThrownBy(() -> jpaUtil.createOrder(cb, path, orderField))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    @Tag("isDescendingOrdering")
    void shouldReturnTrueIfStringStartWithMinusSign() {
        String strWithMinusSign = "-dummy";

        boolean actual = jpaUtil.isDescendingOrdering(strWithMinusSign);

        assertThat(actual).isTrue();
    }

    @Test
    @Tag("isDescendingOrdering")
    void shouldReturnFalseIfStringDoesNotStartWithMinusSign() {
        String withoutMinusSign = "dummy";

        boolean actual = jpaUtil.isDescendingOrdering(withoutMinusSign);

        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @Tag("isDescendingOrdering")
    void shouldThrowExceptionIfGivenStringForCheckingIsEmpty(String str) {
        assertThatThrownBy(() -> jpaUtil.isDescendingOrdering(str))
                .isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class);
    }

    @Test
    @Tag("removeOrderingSign")
    void shouldRemoveMinusSign() {
        String strWithMinusSign = "-dummy";
        String expected = "dummy";

        String actual = jpaUtil.removeOrderingSign(strWithMinusSign);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @Tag("removeOrderingSign")
    void shouldThrowExceptionIfGivenStringForRemovingSignIsEmpty(String str) {
        assertThatThrownBy(() -> jpaUtil.removeOrderingSign(str))
                .isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class);
    }
}
