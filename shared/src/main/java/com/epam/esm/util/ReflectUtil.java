package com.epam.esm.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ReflectUtil {

    private ReflectUtil() {
    }

    /**
     * Checks if the given {@code class} contains declared field
     * with the given {@code name}.
     *
     * @param clazz     target class.
     * @param fieldName name of the declared field.
     * @return {@code true} if contains, {@code false} otherwise.
     */
    public static boolean isContainsField(Class<?> clazz, String fieldName) {
        return ObjectUtils.allNotNull(FieldUtils.getField(clazz, fieldName, true));
    }
}
