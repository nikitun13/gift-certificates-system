package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CreateOrderDetailDto;
import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.DetailedOrderDto;
import com.epam.esm.dto.GiftCertificateDtoWithoutTags;
import com.epam.esm.dto.OrderDetailDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderDetail;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.mapper.OrderMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl service;

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderMapper mapper;
    @Mock
    private UserDao userDao;
    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Test
    @Tag("findByUserId")
    void shouldDelegateFindByIdToDaoAndThenMappingToMapper() {
        LocalDateTime now = LocalDateTime.now();
        Long userId = 5L;
        User user = User.builder().id(userId).email("user").build();
        Order first = new Order(1L, user, now, now, 5432L);
        Order second = new Order(2L, user, now, now, 12345L);
        List<Order> orders = List.of(first, second);
        Pageable pageable = PageRequest.of(0, 20);
        Page<Order> page = new PageImpl<>(orders, pageable, orders.size());
        OrderDto firstDto = new OrderDto(1L, now, now, 5432L);
        OrderDto secondDto = new OrderDto(2L, now, now, 12345L);
        List<OrderDto> orderDtoList = List.of(firstDto, secondDto);
        Page<OrderDto> expected = new PageImpl<>(orderDtoList, pageable, orderDtoList.size());
        doReturn(page)
                .when(orderDao)
                .findByUser(user, pageable);
        doReturn(secondDto)
                .when(mapper)
                .toOrderDto(second);
        doReturn(firstDto)
                .when(mapper)
                .toOrderDto(first);
        doReturn(Optional.of(user))
                .when(userDao)
                .findById(userId);

        Page<OrderDto> actual = service.findByUserId(userId, pageable);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByUserId")
    void shouldThrowEntityNotFoundIfNoSuchUserId() {
        Long userId = 5L;
        Pageable pageable = PageRequest.of(0, 20);
        doReturn(Optional.empty())
                .when(userDao)
                .findById(userId);

        assertThatThrownBy(() -> service.findByUserId(userId, pageable))
                .isExactlyInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @Tag("findByUserIdAndId")
    void shouldDelegateFindByIdToUserDaoThenFindByUserAndIdToOrderDaoAndMappingToMapper() {
        LocalDateTime now = LocalDateTime.now();
        Long userId = 5L;
        Long id = 2L;
        User user = User.builder().id(userId).email("user").build();
        Order order = new Order(id, user, now, now, 5432L);
        OrderDetail first = new OrderDetail(1L, GiftCertificate.builder().id(5L).build(), order, 432L, 2);
        OrderDetail second = new OrderDetail(4L, GiftCertificate.builder().id(3L).build(), order, 245L, 5);
        List<OrderDetail> details = List.of(first, second);
        order.setDetails(details);
        GiftCertificateDtoWithoutTags firstCertificate = new GiftCertificateDtoWithoutTags(
                5L, null, null, null, null, null, null);
        GiftCertificateDtoWithoutTags secondCertificate = new GiftCertificateDtoWithoutTags(
                3L, null, null, null, null, null, null);
        OrderDetailDto firstOrderDetailDto = new OrderDetailDto(1L, 432L, 2, firstCertificate);
        OrderDetailDto secondOrderDetailDto = new OrderDetailDto(4L, 245L, 5, secondCertificate);
        List<OrderDetailDto> dtos = List.of(firstOrderDetailDto, secondOrderDetailDto);
        DetailedOrderDto orderDto = new DetailedOrderDto(id, now, now, 5432L, dtos);
        doReturn(Optional.of(user))
                .when(userDao)
                .findById(userId);
        doReturn(Optional.of(order))
                .when(orderDao)
                .findByUserAndId(user, id);
        doReturn(orderDto)
                .when(mapper)
                .toDetailedOrderDto(order);
        Optional<DetailedOrderDto> expected = Optional.of(orderDto);

        Optional<DetailedOrderDto> actual = service.findByUserIdAndId(userId, id);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByUserIdAndId")
    void shouldThrowEntityNotFoundIfNoSuchUser() {
        Long userId = 500L;
        Long id = 2L;
        doReturn(Optional.empty())
                .when(userDao)
                .findById(userId);

        assertThatThrownBy(() -> service.findByUserIdAndId(userId, id))
                .isExactlyInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @Tag("findByUserIdAndId")
    void shouldReturnEmptyOptionalIfNoSuchOrderIdForTheGivenUser() {
        Long userId = 5L;
        Long id = 200L;
        User user = User.builder().id(userId).email("user").build();
        doReturn(Optional.of(user))
                .when(userDao)
                .findById(userId);
        doReturn(Optional.empty())
                .when(orderDao)
                .findByUserAndId(user, id);

        Optional<DetailedOrderDto> actual = service.findByUserIdAndId(userId, id);

        assertThat(actual).isEmpty();
    }

    @Test
    @Tag("create")
    void shouldDelegateToDaosAndCountTotalPrice() {
        Long userId = 5L;
        User user = User.builder().id(userId).email("user").build();
        Long firstCertificateId = 3L;
        Long secondCertificateId = 5L;
        GiftCertificate firstCertificate = GiftCertificate.builder().id(firstCertificateId).price(100L).build();
        GiftCertificate secondCertificate = GiftCertificate.builder().id(secondCertificateId).price(200L).build();
        CreateOrderDetailDto firstDetailDto = new CreateOrderDetailDto(firstCertificateId, 2);
        CreateOrderDetailDto secondDetailDto = new CreateOrderDetailDto(secondCertificateId, 5);
        List<CreateOrderDetailDto> detailDtoList = List.of(firstDetailDto, secondDetailDto);
        CreateOrderDto createOrderDto = new CreateOrderDto(detailDtoList);
        Order order = new Order(null, user, null, null, null);
        OrderDetail first = new OrderDetail(null, GiftCertificate.builder().id(firstCertificateId).build(), order, null, 2);
        OrderDetail second = new OrderDetail(null, GiftCertificate.builder().id(secondCertificateId).build(), order, null, 5);
        List<OrderDetail> details = List.of(first, second);
        order.setDetails(details);
        doReturn(Optional.of(user))
                .when(userDao)
                .findById(userId);
        doReturn(order)
                .when(mapper)
                .toOrder(createOrderDto, user);
        doReturn(Optional.of(firstCertificate))
                .when(giftCertificateDao)
                .findById(firstCertificateId);
        doReturn(Optional.of(secondCertificate))
                .when(giftCertificateDao)
                .findById(secondCertificateId);
        Order updatedOrder = new Order(null, user, null, null, 1200L);
        OrderDetail firstUpdatedDetail = new OrderDetail(null, firstCertificate, updatedOrder, firstCertificate.getPrice(), 2);
        OrderDetail secondUpdatedDetail = new OrderDetail(null, secondCertificate, updatedOrder, secondCertificate.getPrice(), 5);
        List<OrderDetail> updatedDetails = List.of(firstUpdatedDetail, secondUpdatedDetail);
        updatedOrder.setDetails(updatedDetails);
        LocalDateTime now = LocalDateTime.now();
        doAnswer(invocation -> {
            Order arg = invocation.getArgument(0);
            arg.setId(8L);
            long id = 5L;
            arg.setCreateDate(now);
            arg.setLastUpdateDate(now);
            for (OrderDetail detail : arg.getDetails()) {
                detail.setId(++id);
            }
            return arg;
        })
                .when(orderDao)
                .create(updatedOrder);
        Order fullOrder = new Order(8L, user, now, now, 1200L);
        OrderDetail firstFullDetail = new OrderDetail(6L, firstCertificate, fullOrder, firstCertificate.getPrice(), 2);
        OrderDetail secondFullDetail = new OrderDetail(7L, secondCertificate, fullOrder, secondCertificate.getPrice(), 5);
        List<OrderDetail> list = List.of(firstFullDetail, secondFullDetail);
        fullOrder.setDetails(list);
        GiftCertificateDtoWithoutTags firstCertificateWithoutTags = new GiftCertificateDtoWithoutTags(
                firstCertificateId, null, null, firstCertificate.getPrice(), null, null, null);
        GiftCertificateDtoWithoutTags secondCertificateWithoutTags = new GiftCertificateDtoWithoutTags(
                secondCertificateId, null, null, secondCertificate.getPrice(), null, null, null);
        OrderDetailDto firstOrderDetailDto = new OrderDetailDto(6L, firstCertificate.getPrice(), 2, firstCertificateWithoutTags);
        OrderDetailDto secondOrderDetailDto = new OrderDetailDto(7L, firstCertificate.getPrice(), 5, secondCertificateWithoutTags);
        List<OrderDetailDto> detailDtos = List.of(firstOrderDetailDto, secondOrderDetailDto);
        DetailedOrderDto expected = new DetailedOrderDto(8L, now, now, 1200L, detailDtos);
        doReturn(expected)
                .when(mapper)
                .toDetailedOrderDto(fullOrder);

        DetailedOrderDto actual = service.create(createOrderDto, userId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("create")
    void shouldThrowEntityNotFoundIfNoSuchUserForCreateMethod() {
        Long userId = 500L;
        doReturn(Optional.empty())
                .when(userDao)
                .findById(userId);
        Long firstCertificateId = 3L;
        Long secondCertificateId = 5L;
        CreateOrderDetailDto firstDetailDto = new CreateOrderDetailDto(firstCertificateId, 2);
        CreateOrderDetailDto secondDetailDto = new CreateOrderDetailDto(secondCertificateId, 5);
        List<CreateOrderDetailDto> detailDtoList = List.of(firstDetailDto, secondDetailDto);
        CreateOrderDto createOrderDto = new CreateOrderDto(detailDtoList);

        assertThatThrownBy(() -> service.create(createOrderDto, userId))
                .isExactlyInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @Tag("create")
    void shouldThrowEntityNotFoundIfNoGiftCertificateIdForCreateMethod() {
        Long userId = 5L;
        User user = User.builder().id(userId).email("user").build();
        Long firstCertificateId = 300L;
        CreateOrderDetailDto firstDetailDto = new CreateOrderDetailDto(firstCertificateId, 2);
        Order order = new Order(null, user, null, null, null);
        OrderDetail first = new OrderDetail(
                null, GiftCertificate.builder().id(firstCertificateId).build(), order, null, 2);
        List<OrderDetail> details = List.of(first);
        order.setDetails(details);
        List<CreateOrderDetailDto> detailDtoList = List.of(firstDetailDto);
        CreateOrderDto createOrderDto = new CreateOrderDto(detailDtoList);
        doReturn(Optional.empty())
                .when(giftCertificateDao)
                .findById(firstCertificateId);
        doReturn(Optional.of(user))
                .when(userDao)
                .findById(userId);
        doReturn(order)
                .when(mapper)
                .toOrder(createOrderDto, user);

        assertThatThrownBy(() -> service.create(createOrderDto, userId))
                .isExactlyInstanceOf(EntityNotFoundException.class);
    }
}
