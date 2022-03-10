package com.epam.esm.mapper;

import com.epam.esm.MapperTestConfig;
import com.epam.esm.dto.CreateOrderDetailDto;
import com.epam.esm.dto.GiftCertificateDtoWithoutTags;
import com.epam.esm.dto.OrderDetailDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderDetail;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = MapperTestConfig.class)
@ActiveProfiles("test")
class OrderDetailMapperTest {

    @InjectMocks
    private OrderDetailMapperImpl mapper;

    @Mock
    private GiftCertificateMapper giftCertificateMapper;

    public static Stream<Arguments> orderDetailToOrderDetailDtoData() {
        GiftCertificateDtoWithoutTags giftCertificateDto = new GiftCertificateDtoWithoutTags(
                1L, null, null, null, null, null, null);
        GiftCertificate giftCertificate = GiftCertificate.builder().id(1L).build();
        return Stream.of(
                Arguments.of(new OrderDetail(1L, giftCertificate, null, 1234L, 3),
                        new OrderDetailDto(1L, 1234L, 3, giftCertificateDto)),
                Arguments.of(new OrderDetail(null, giftCertificate, null, 1234L, 3),
                        new OrderDetailDto(null, 1234L, 3, giftCertificateDto)),
                Arguments.of(new OrderDetail(1L, giftCertificate, null, null, 3),
                        new OrderDetailDto(1L, null, 3, giftCertificateDto)),
                Arguments.of(new OrderDetail(1L, giftCertificate, null, 1234L, null),
                        new OrderDetailDto(1L, 1234L, null, giftCertificateDto)),
                Arguments.of(new OrderDetail(null, giftCertificate, null, null, null),
                        new OrderDetailDto(null, null, null, giftCertificateDto)),
                Arguments.of(new OrderDetail(1L, giftCertificate, null, null, null),
                        new OrderDetailDto(1L, null, null, giftCertificateDto)),
                Arguments.of(new OrderDetail(null, giftCertificate, null, 1234L, 3),
                        new OrderDetailDto(null, 1234L, 3, giftCertificateDto))
        );
    }

    public static Stream<Arguments> createOrderDetailDtoToOrderDetailData() {
        Order order = new Order();
        order.setId(5L);
        return Stream.of(
                Arguments.of(new CreateOrderDetailDto(5L, 3), order,
                        new OrderDetail(null, GiftCertificate.builder().id(5L).build(), order, null, 3)),
                Arguments.of(new CreateOrderDetailDto(5L, 3), null,
                        new OrderDetail(null, GiftCertificate.builder().id(5L).build(), null, null, 3)),
                Arguments.of(new CreateOrderDetailDto(null, 3), order,
                        new OrderDetail(null, GiftCertificate.builder().build(), order, null, 3)),
                Arguments.of(new CreateOrderDetailDto(5L, null), order,
                        new OrderDetail(null, GiftCertificate.builder().id(5L).build(), order, null, null)),
                Arguments.of(new CreateOrderDetailDto(null, null), order,
                        new OrderDetail(null, GiftCertificate.builder().build(), order, null, null)),
                Arguments.of(new CreateOrderDetailDto(null, null), null,
                        new OrderDetail(null, GiftCertificate.builder().build(), null, null, null))
        );
    }

    @ParameterizedTest
    @MethodSource("orderDetailToOrderDetailDtoData")
    @Tag("toOrderDetailDto")
    void toOrderDetailDto(OrderDetail orderDetail, OrderDetailDto expected) {
        GiftCertificateDtoWithoutTags giftCertificateDto = new GiftCertificateDtoWithoutTags(
                1L, null, null, null, null, null, null);
        doReturn(giftCertificateDto)
                .when(giftCertificateMapper)
                .toGiftCertificateDtoWithoutTags(GiftCertificate.builder().id(1L).build());
        OrderDetailDto actual = mapper.toOrderDetailDto(orderDetail);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("createOrderDetailDtoToOrderDetailData")
    @Tag("toOrderDetail")
    void toOrderDetail(CreateOrderDetailDto createOrderDetailDto, Order order, OrderDetail expected) {
        OrderDetail actual = mapper.toOrderDetail(createOrderDetailDto, order);

        assertThat(actual).isEqualTo(expected);
    }
}
