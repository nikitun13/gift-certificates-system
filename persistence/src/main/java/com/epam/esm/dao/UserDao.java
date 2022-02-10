package com.epam.esm.dao;

import com.epam.esm.entity.User;

/**
 * The interface {@code UserDao} is an interface that extends {@link BaseDao}
 * and provides new operations with {@link User} entities in the database.
 *
 * @see BaseDao
 * @see User
 */
public interface UserDao extends BaseDao<Long, User> {
}
