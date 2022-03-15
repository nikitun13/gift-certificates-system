package com.epam.esm.mapper;

import com.epam.esm.MapperTestConfig;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = MapperTestConfig.class)
@ActiveProfiles("test")
class OrderMapperTest {

    @InjectMocks
    private OrderMapperImpl mapper;

    @Mock
    private OrderDetailMapper orderDetailMapper;

    public static Stream<Arguments> orderToOrderDtoData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(new Order(1L, User.builder().id(1L).build(), now, now, 5432L),
                        new OrderDto(1L, now, now, 5432L)),
                Arguments.of(new Order(null, null, null, null, null),
                        new OrderDto(null, null, null, null)),
                Arguments.of(new Order(5L, null, now, now, 542L),
                        new OrderDto(5L, now, now, 542L)),
                Arguments.of(new Order(null, null, now, now, 542L),
                        new OrderDto(null, now, now, 542L)),
                Arguments.of(new Order(4L, null, null, now, 542L),
                        new OrderDto(4L, null, now, 542L)),
                Arguments.of(new Order(4L, null, now, null, 542L),
                        new OrderDto(4L, now, null, 542L)),
                Arguments.of(new Order(4L, null, now, now, null),
                        new OrderDto(4L, now, now, null)),
                Arguments.of(new Order(4L, null, null, null, null),
                        new OrderDto(4L, null, null, null))
        );
    }

    @ParameterizedTest
    @MethodSource("orderToOrderDtoData")
    @Tag("toOrderDto")
    void toOrderDto(Order order, OrderDto expected) {
        OrderDto actual = mapper.toOrderDto(order);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("toDetailedOrderDto")
    void toDetailedOrderDto() {
        User user = User.builder().id(6L).username("username").build();
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(1L, user, now, now, 5432L);
        OrderDetail firstDetail = new OrderDetail(
                1L, GiftCertificate.builder().id(1L).build(), order, 1234L, 3);
        OrderDetail secondDetail = new OrderDetail(
                5L, GiftCertificate.builder().id(5L).build(), order, 321L, 2);
        OrderDetail thirdDetail = new OrderDetail(
                9L, GiftCertificate.builder().id(2L).build(), order, 54L, 1);
        order.setDetails(List.of(firstDetail, secondDetail, thirdDetail));
        GiftCertificateDtoWithoutTags firstCertificate = new GiftCertificateDtoWithoutTags(1L,
                null, null, null, null, null, null);
        GiftCertificateDtoWithoutTags secondCertificate = new GiftCertificateDtoWithoutTags(5L,
                null, null, null, null, null, null);
        GiftCertificateDtoWithoutTags thirdCertificate = new GiftCertificateDtoWithoutTags(2L,
                null, null, null, null, null, null);
        OrderDetailDto first = new OrderDetailDto(
                firstDetail.getId(), firstDetail.getPrice(), firstDetail.getQuantity(), firstCertificate);
        OrderDetailDto second = new OrderDetailDto(
                secondDetail.getId(), secondDetail.getPrice(), secondDetail.getQuantity(), secondCertificate);
        OrderDetailDto third = new OrderDetailDto(
                thirdDetail.getId(), thirdDetail.getPrice(), thirdDetail.getQuantity(), thirdCertificate);
        doReturn(first)
                .when(orderDetailMapper)
                .toOrderDetailDto(firstDetail);
        doReturn(second)
                .when(orderDetailMapper)
                .toOrderDetailDto(secondDetail);
        doReturn(third)
                .when(orderDetailMapper)
                .toOrderDetailDto(thirdDetail);
        DetailedOrderDto expected = new DetailedOrderDto(1L, now, now, 5432L,
                List.of(first, second, third));

        DetailedOrderDto actual = mapper.toDetailedOrderDto(order);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("toOrder")
    void shouldConvertToOrderIfDetailsAreEmpty() {
        CreateOrderDto dto = new CreateOrderDto(Collections.emptyList());
        User user = User.builder().id(1L).build();
        Order expected = new Order();
        expected.setUser(user);

        Order actual = mapper.toOrder(dto, user);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("toOrder")
    void shouldConvertToOrderWithDetails() {
        CreateOrderDetailDto first = new CreateOrderDetailDto(1L, 3);
        CreateOrderDetailDto second = new CreateOrderDetailDto(5L, 2);
        CreateOrderDetailDto third = new CreateOrderDetailDto(2L, 1);
        CreateOrderDto dto = new CreateOrderDto(List.of(first, second, third));
        User user = User.builder().id(1L).build();
        Order expected = new Order();
        expected.setUser(user);
        OrderDetail firstDetail = new OrderDetail(
                null, GiftCertificate.builder().id(1L).build(), expected, null, 3);
        OrderDetail secondDetail = new OrderDetail(
                null, GiftCertificate.builder().id(5L).build(), expected, null, 2);
        OrderDetail thirdDetail = new OrderDetail(
                null, GiftCertificate.builder().id(2L).build(), expected, null, 1);
        doReturn(firstDetail)
                .when(orderDetailMapper)
                .toOrderDetail(first, expected);
        doReturn(secondDetail)
                .when(orderDetailMapper)
                .toOrderDetail(second, expected);
        doReturn(thirdDetail)
                .when(orderDetailMapper)
                .toOrderDetail(third, expected);

        Order actual = mapper.toOrder(dto, user);
        expected.setDetails(List.of(firstDetail, secondDetail, thirdDetail));

        assertThat(actual).isEqualTo(expected);
    }
}
