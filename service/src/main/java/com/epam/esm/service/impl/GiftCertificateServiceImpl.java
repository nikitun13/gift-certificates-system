package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateFilters;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateMapper giftCertificateMapper;

    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao,
                                      GiftCertificateMapper giftCertificateMapper) {
        this.giftCertificateDao = giftCertificateDao;
        this.giftCertificateMapper = giftCertificateMapper;
    }

    @Override
    public Page<GiftCertificateDto> findAll(GiftCertificateFilters filters, Pageable pageable) {
        return giftCertificateDao.findAll(filters, pageable)
                .map(giftCertificateMapper::toGiftCertificateDto);
    }

    @Override
    public Optional<GiftCertificateDto> findById(Long id) {
        return giftCertificateDao.findById(id)
                .map(giftCertificateMapper::toGiftCertificateDto);
    }

    @Override
    public boolean update(UpdateGiftCertificateDto updateGiftCertificateDto, Long id) {
        GiftCertificate entity = giftCertificateMapper.toGiftCertificate(updateGiftCertificateDto);
        entity.setId(id);
        return giftCertificateDao.update(entity);
    }

    @Override
    public boolean delete(Long id) {
        return giftCertificateDao.delete(id);
    }

    @Override
    public GiftCertificateDto create(UpdateGiftCertificateDto createGiftCertificateDto) {
        GiftCertificate entity = giftCertificateMapper.toGiftCertificate(createGiftCertificateDto);
        giftCertificateDao.create(entity);
        return giftCertificateMapper.toGiftCertificateDto(entity);
    }
}
