package com.epam.esm.util;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

@Component
public class JpaUtil {

    private static final String ORDER_FIELD_EXCEPTION_MESSAGE = "Order field can't be empty";

    /**
     * Wraps string with percentages.
     *
     * @param str string to be wrapped.
     * @return wrapped string.
     */
    public String wrapWithPercentages(String str) {
        ObjectUtils.requireNonEmpty(str, "String can't be empty");
        return "%" + str + "%";
    }

    /**
     * Creates {@link Order} by the given {@code field} in the given {@code entity}.
     * Order field may contain {@code '-'} sign at the beginning,
     * that means to sort by this field in descending order.
     *
     * @param cb         {@link CriteriaBuilder} for creating {@link Order}.
     * @param path       {@link Path} to entity.
     * @param orderField entity field that may contain {@code '-'} sign at the beginning.
     * @param <T>        entity type.
     * @return {@link Order} by the given {@code field} in corresponding ordering.
     */
    public <T> Order createOrder(CriteriaBuilder cb, Path<T> path, String orderField) {
        if (ObjectUtils.anyNull(cb, path, orderField)) {
            throw new NullPointerException("Args can't be null");
        }
        if (isDescendingOrdering(orderField)) {
            String field = removeOrderingSign(orderField);
            return cb.desc(path.get(field));
        } else {
            return cb.asc(path.get(orderField));
        }
    }

    /**
     * Checks if a string starts with {@code '-'} sign, that means
     * to sort by this field in descending order.
     *
     * @param orderField field name that maybe contains {@code '-'} sign.
     * @return {@code true} if field contains {@code '-'} and ordering should be descending,
     * {@code false} otherwise.
     */
    public boolean isDescendingOrdering(String orderField) {
        ObjectUtils.requireNonEmpty(orderField, ORDER_FIELD_EXCEPTION_MESSAGE);
        return orderField.startsWith("-");
    }

    /**
     * Removes first character from the string.
     * It should be {@code '-'} sign, that means
     * to sort by this field in descending order.
     *
     * @param orderField string with field name and ordering sign.
     * @return field name without ordering sign.
     */
    public String removeOrderingSign(String orderField) {
        ObjectUtils.requireNonEmpty(orderField, ORDER_FIELD_EXCEPTION_MESSAGE);
        return orderField.substring(1);
    }
}
