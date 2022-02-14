package com.epam.esm.service;

import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.DetailedOrderDto;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {

    /**
     * Finds orders by the given {@code userId}.
     *
     * @param userId   {@code id} of the {@code User}.
     * @param pageable pagination information.
     * @return list of {@code orders} by the given {@code userId}.
     */
    Page<OrderDto> findByUserId(Long userId, Pageable pageable);

    /**
     * Finds order with details by the given {@code userId} and {@code id}.
     *
     * @param userId {@code id} of the {@code User}.
     * @param id     {@code id} of the order.
     * @return {@link Optional} {@link DetailedOrderDto}.
     * Empty optional if {@code order} wasn't found.
     */
    Optional<DetailedOrderDto> findByUserIdAndId(Long userId, Long id);

    /**
     * Creates new {@code Order} in the storage.
     * Uses {@link CreateOrderDto} and {@code userId} for creation.
     *
     * @param createOrderDto dto that contains information for creation.
     * @param userId         {@code id} of the user who makes the order.
     * @return created entity that mapped to {@link GiftCertificateDto}.
     */
    DetailedOrderDto create(CreateOrderDto createOrderDto, Long userId);
}
