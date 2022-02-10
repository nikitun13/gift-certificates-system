package com.epam.esm.service;

import com.epam.esm.dto.DetailedOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.Page;
import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    /**
     * Finds orders by the given {@code User}.
     *
     * @param userDto {@code dto} of the {@code User}.
     * @param page    page for limit and offset.
     * @return list of {@code orders} by the given {@code User}.
     */
    List<OrderDto> findByUser(UserDto userDto, Page page);

    /**
     * Finds order with details by the given {@code User} and {@code id}.
     *
     * @param userDto {@code dto} of the {@code User}.
     * @param id      {@code id} of the order.
     * @return {@link Optional} {@code order} with details.
     */
    Optional<DetailedOrderDto> findByUserAndId(UserDto userDto, Long id);
}
