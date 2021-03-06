package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.DetailedOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderDetail;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderMapper orderMapper;
    private final UserDao userDao;
    private final GiftCertificateDao giftCertificateDao;

    public OrderServiceImpl(OrderDao orderDao,
                            OrderMapper orderMapper,
                            UserDao userDao,
                            GiftCertificateDao giftCertificateDao) {
        this.orderDao = orderDao;
        this.orderMapper = orderMapper;
        this.userDao = userDao;
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public Page<OrderDto> findByUserId(Long userId, Pageable pageable) {
        User user = userDao.findById(userId).orElseThrow(() -> new EntityNotFoundException(userId));
        return orderDao.findByUser(user, pageable)
                .map(orderMapper::toOrderDto);
    }

    @Override
    public Optional<DetailedOrderDto> findByUserIdAndId(Long userId, Long id) {
        User user = userDao.findById(userId).orElseThrow(() -> new EntityNotFoundException(userId));
        return orderDao.findByUserAndId(user, id)
                .map(orderMapper::toDetailedOrderDto);
    }

    @Override
    public DetailedOrderDto create(CreateOrderDto createOrderDto, Long userId) {
        User user = userDao.findById(userId).orElseThrow(() -> new EntityNotFoundException(userId));
        Order order = orderMapper.toOrder(createOrderDto, user);
        long totalPrice = 0L;
        for (OrderDetail detail : order.getDetails()) {
            Long giftCertificateId = detail.getGiftCertificate().getId();
            GiftCertificate giftCertificate = giftCertificateDao.findById(giftCertificateId)
                    .orElseThrow(() -> new EntityNotFoundException(giftCertificateId));
            detail.setGiftCertificate(giftCertificate);
            Long price = giftCertificate.getPrice();
            detail.setPrice(price);
            totalPrice += detail.countTotalPrice();
        }
        order.setTotalPrice(totalPrice);
        orderDao.create(order);
        return orderMapper.toDetailedOrderDto(order);
    }
}
