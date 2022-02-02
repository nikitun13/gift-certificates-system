package com.epam.esm.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

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
        ObjectUtils.requireNonEmpty(clazz, "Class can't be null");
        if (StringUtils.isEmpty(fieldName)) {
            return false;
        }
        Field field = FieldUtils.getField(clazz, fieldName, true);
        return ObjectUtils.allNotNull(field);
    }

    /**
     * Returns map of the all field names with corresponding values.
     * Values are wrapped to {@link Optional}.
     *
     * @param o object whose fields and values are required.
     * @return map of the all field names with corresponding values.
     */
    public static Map<String, Optional<Object>> getAllFieldNamesWithValues(Object o) {
        ObjectUtils.requireNonEmpty(o, "Object can't be null");
        Class<?> clazz = o.getClass();
        return FieldUtils.getAllFieldsList(clazz).stream()
                .map(Field::getName)
                .map(name -> BeanUtils.getPropertyDescriptor(clazz, name))
                .collect(toMap(FeatureDescriptor::getName,
                        descriptor -> Optional.ofNullable(ReflectionUtils.invokeMethod(descriptor.getReadMethod(), o))
                ));
    }
}
