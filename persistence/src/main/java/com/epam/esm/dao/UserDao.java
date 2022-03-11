package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.Optional;

/**
 * The interface {@code UserDao} is an interface that extends {@link BaseDao}
 * and provides new operations with {@link User} entities in the database.
 *
 * @see BaseDao
 * @see User
 */
public interface UserDao extends BaseDao<Long, User> {

    /**
     * Finds {@link User} by {@code username}.
     *
     * @param username {@code username} of the {@link User}.
     * @return Optional {@link User}. If the given {@code username} exists,
     * {@link Optional} contains corresponding user, empty {@link Optional} otherwise.
     */
    Optional<User> findByUsername(String username);
}
