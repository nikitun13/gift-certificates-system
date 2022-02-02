package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

/**
 * Describes the interface of the generic {@code dao}, which provides
 * basic CRUD (Create, Reed, Update, Delete) operations with entities in the database.
 *
 * @param <K> id (key) of the entity.
 * @param <E> entity itself.
 */
public interface BaseDao<K, E> {

    /**
     * Returns all {@code entities} from the repository.
     *
     * @return list of the all {@code entities} from the repository.
     */
    List<E> findAll();

    /**
     * Finds {@code entity} by its {@code id}.
     *
     * @param id {@code id} of the {@code entity}.
     * @return Optional {@code entity}. If the given {@code id} exists,
     * {@link Optional} contains corresponding entity, empty {@link Optional} otherwise.
     */
    Optional<E> findById(K id);

    /**
     * Adds new {@code entity} to the repository.
     *
     * @param entity {@code entity} to be added to the repository.
     */
    void create(E entity);

    /**
     * Updates {@code entity} in the repository.
     *
     * @param entity {@code entity} to be updated in the repository.
     * @return {@code true} if {@code entity} was updated successfully,
     * {@code false otherwise}.
     */
    boolean update(E entity);

    /**
     * Deletes {@code entity} from the repository by its {@code id}.
     *
     * @param id of the {@code entity} to be deleted.
     * @return {@code true} if {@code entity} was deleted successfully,
     * {@code false otherwise}.
     */
    boolean delete(K id);
}
