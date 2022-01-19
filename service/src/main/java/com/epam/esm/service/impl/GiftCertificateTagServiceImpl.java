package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagService;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GiftCertificateTagServiceImpl implements GiftCertificateTagService {

    private final GiftCertificateService giftCertificateService;
    private final TagService tagService;
    private final GiftCertificateTagDao giftCertificateTagDao;

    @Autowired
    public GiftCertificateTagServiceImpl(GiftCertificateService giftCertificateService,
                                         TagService tagService,
                                         GiftCertificateTagDao giftCertificateTagDao) {
        this.giftCertificateService = giftCertificateService;
        this.tagService = tagService;
        this.giftCertificateTagDao = giftCertificateTagDao;
    }

    @Override
    @Transactional
    public void update(UpdateGiftCertificateDto updateGiftCertificateDto, Long giftCertificateId) {
        giftCertificateService.update(updateGiftCertificateDto, giftCertificateId);
        updateGiftCertificateDto.tags().stream()
                .map(tagService::createIfNotExists)
                .map(TagDto::id)
                .forEach(id -> giftCertificateTagDao.create(giftCertificateId, id));
    }

    @Override
    @Transactional
    public GiftCertificateDto create(UpdateGiftCertificateDto createGiftCertificateDto) {
        GiftCertificateDto createdGiftCertificate = giftCertificateService.create(createGiftCertificateDto);
        Long giftCertificateId = createdGiftCertificate.id();
        List<TagDto> tags = createGiftCertificateDto.tags().stream()
                .map(tagService::createIfNotExists)
                .toList();
        tags.stream()
                .map(TagDto::id)
                .forEach(id -> giftCertificateTagDao.create(giftCertificateId, id));
        createdGiftCertificate.tags().addAll(tags);
        return createdGiftCertificate;
    }
}
