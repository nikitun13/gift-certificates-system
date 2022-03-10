package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;

/**
 * The interface {@code TagDao} is an interface that extends {@link BaseDao}
 * and provides new operations with {@link Tag} entities in the database.
 *
 * @see BaseDao
 * @see Tag
 */
public interface TagDao extends BaseDao<Long, Tag> {

    /**
     * Finds {@code entity} by its {@code name}.
     *
     * @param name unique {@code name} of the {@code entity}.
     * @return Optional {@code entity}. If the given {@code id} exists,
     * {@link Optional} contains corresponding entity, empty {@link Optional} otherwise.
     */
    Optional<Tag> findByName(String name);

    /**
     * Creates new {@link Tag} in the storage if not exists.
     * Returns new or existing {@link Tag}.
     *
     * @param tag {@code tag} to be created if not exists.
     * @return created or existing entity.
     */
    Tag createIfNotExists(Tag tag);

    /**
     * Finds the most widely used tag of a user
     * with the highest cost of all orders.
     *
     * @return {@link Optional} {@link Tag}.
     * Empty optional if nothing found.
     */
    Optional<Tag> findTopTagOfUserWithTheHighestCostOfAllOrders();
}
