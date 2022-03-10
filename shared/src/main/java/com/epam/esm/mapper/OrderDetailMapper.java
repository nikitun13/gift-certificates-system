package com.epam.esm.mapper;

import com.epam.esm.dto.CreateOrderDetailDto;
import com.epam.esm.dto.GiftCertificateDtoWithoutTags;
import com.epam.esm.dto.OrderDetailDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", imports = GiftCertificate.class)
public abstract class OrderDetailMapper {

    @Autowired
    protected GiftCertificateMapper giftCertificateMapper;

    public abstract OrderDetailDto toOrderDetailDto(OrderDetail orderDetail);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "giftCertificate",
            expression = "java(GiftCertificate.builder().id(createOrderDetailDto.giftCertificateId()).build())")
    public abstract OrderDetail toOrderDetail(CreateOrderDetailDto createOrderDetailDto, Order order);

    protected GiftCertificateDtoWithoutTags toGiftCertificateDto(GiftCertificate giftCertificate) {
        return giftCertificateMapper.toGiftCertificateDtoWithoutTags(giftCertificate);
    }
}
