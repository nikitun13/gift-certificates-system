package com.epam.esm.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDetailDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class OrderDetailMapper {

    @Autowired
    protected GiftCertificateMapper giftCertificateMapper;

    public abstract OrderDetailDto toOrderDetailDto(OrderDetail orderDetail);

    protected GiftCertificateDto toGiftCertificateDto(GiftCertificate giftCertificate) {
        return giftCertificateMapper.toGiftCertificateDto(giftCertificate);
    }
}
