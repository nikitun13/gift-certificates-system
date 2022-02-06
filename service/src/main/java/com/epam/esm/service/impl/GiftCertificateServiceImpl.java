package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Page;
import com.epam.esm.mapper.Mapper;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final Mapper<GiftCertificate, GiftCertificateDto> giftCertificateDtoMapper;
    private final Mapper<GiftCertificate, UpdateGiftCertificateDto> updateGiftCertificateDtoMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao,
                                      Mapper<GiftCertificate, GiftCertificateDto> giftCertificateDtoMapper,
                                      Mapper<GiftCertificate, UpdateGiftCertificateDto> updateGiftCertificateDtoMapper) {
        this.giftCertificateDao = giftCertificateDao;
        this.giftCertificateDtoMapper = giftCertificateDtoMapper;
        this.updateGiftCertificateDtoMapper = updateGiftCertificateDtoMapper;
    }

    @Override
    public List<GiftCertificateDto> findAll(Page page) {
        return giftCertificateDao.findAll(page).stream()
                .map(giftCertificateDtoMapper::mapToDto)
                .toList();
    }

    @Override
    public Optional<GiftCertificateDto> findById(Long id) {
        return giftCertificateDao.findById(id)
                .map(giftCertificateDtoMapper::mapToDto);
    }

    @Override
    public boolean update(UpdateGiftCertificateDto updateGiftCertificateDto, Long id) {
        GiftCertificate entity = updateGiftCertificateDtoMapper.mapToEntity(updateGiftCertificateDto);
        entity.setId(id);
        LocalDateTime now = LocalDateTime.now();
        entity.setLastUpdateDate(now);
        return giftCertificateDao.update(entity);
    }

    @Override
    public boolean delete(Long id) {
        return giftCertificateDao.delete(id);
    }

    @Override
    public GiftCertificateDto create(UpdateGiftCertificateDto createGiftCertificateDto) {
        GiftCertificate entity = updateGiftCertificateDtoMapper.mapToEntity(createGiftCertificateDto);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateDate(now);
        entity.setLastUpdateDate(now);
        giftCertificateDao.create(entity);
        return giftCertificateDtoMapper.mapToDto(entity);
    }
}
