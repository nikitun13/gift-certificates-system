package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * The interface {@code OrderDao} is an interface that extends {@link BaseDao}
 * and provides new operations with {@link Order} entities in the database.
 *
 * @see BaseDao
 * @see Order
 */
public interface OrderDao extends BaseDao<Long, Order> {

    /**
     * Finds orders by the given {@link User}.
     *
     * @param user     {@link User} whose orders are to be found.
     * @param pageable pagination information.
     * @return list of orders by the given {@code User}.
     */
    Page<Order> findByUser(User user, Pageable pageable);

    /**
     * Finds order by the given {@code User} and {@code id}.
     *
     * @param user {@link User} whose order is to be found.
     * @param id   {@code id} of the order.
     * @return {@link Optional} {@code order}. Empty optional if order is not found.
     */
    Optional<Order> findByUserAndId(User user, Long id);

    /**
     * Counts number of orders in the database
     * by the given {@code User}.
     *
     * @return number of orders by the given {@code user}.
     */
    long countByUser(User user);
}
