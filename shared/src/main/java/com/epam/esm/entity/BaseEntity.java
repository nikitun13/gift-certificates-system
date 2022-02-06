package com.epam.esm.entity;

import java.io.Serializable;

/**
 * Entity classes marker. All entity classes should implement this interface.
 *
 * @param <T> id type of the entity (should extend {@link Serializable}).
 */
public interface BaseEntity<T extends Serializable> {

    /**
     * Setter for {@code id}.
     *
     * @param id new id.
     */
    void setId(T id);

    /**
     * Getter for {@code id}.
     *
     * @return id of the entity.
     */
    T getId();
}
