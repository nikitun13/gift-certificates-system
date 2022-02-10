package com.epam.esm.dao;

import com.epam.esm.dto.Page;
import com.epam.esm.entity.Order;

import java.util.List;
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
     * Finds orders by the given {@code userId}.
     *
     * @param userId {@code id} of the {@code User}.
     * @param page   page for limit and offset.
     * @return list of orders by the given {@code User}.
     */
    List<Order> findByUserId(Long userId, Page page);

    /**
     * Finds order by the given {@code User} and {@code id}.
     *
     * @param userId {@code id} of the {@code User}.
     * @param id     {@code id} of the order.
     * @return {@link Optional} {@code order} with details.
     */
    Optional<Order> findByUserIdAndId(Long userId, Long id);
}
