package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dto.DetailedOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.Page;
import com.epam.esm.dto.UserDto;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderDao orderDao, OrderMapper orderMapper) {
        this.orderDao = orderDao;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<OrderDto> findByUser(UserDto userDto, Page page) {
        return orderDao.findByUserId(userDto.id(), page).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public Optional<DetailedOrderDto> findByUserAndId(UserDto userDto, Long id) {
        return orderDao.findByUserIdAndId(userDto.id(), id)
                .map(orderMapper::toDetailedOrderDto);
    }
}
