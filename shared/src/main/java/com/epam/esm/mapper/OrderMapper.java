package com.epam.esm.mapper;

import com.epam.esm.dto.CreateOrderDetailDto;
import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.DetailedOrderDto;
import com.epam.esm.dto.OrderDetailDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderDetail;
import com.epam.esm.entity.User;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Autowired
    protected OrderDetailMapper orderDetailMapper;

    public abstract OrderDto toOrderDto(Order order);

    public abstract DetailedOrderDto toDetailedOrderDto(Order order);

    public Order toOrder(CreateOrderDto createOrderDto, User user) {
        Order order = new Order();
        order.setUser(user);
        List<OrderDetail> details = toOrderDetailList(createOrderDto.details(), order);
        order.setDetails(details);
        return order;
    }

    protected List<OrderDetailDto> toOrderDetailDtoList(List<OrderDetail> detailedOrders) {
        return detailedOrders.stream()
                .map(orderDetailMapper::toOrderDetailDto)
                .toList();
    }

    protected List<OrderDetail> toOrderDetailList(List<CreateOrderDetailDto> createOrderDetailDtoList,
                                                  Order order) {
        return createOrderDetailDtoList.stream()
                .map(detail -> orderDetailMapper.toOrderDetail(detail, order))
                .toList();
    }
}
