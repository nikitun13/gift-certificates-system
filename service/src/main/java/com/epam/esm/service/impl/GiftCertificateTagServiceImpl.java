package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.ParamParseUtil;
import com.epam.esm.util.ReflectUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Introspector;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class GiftCertificateTagServiceImpl implements GiftCertificateTagService {

    private static final String TAG_PARAMS_PREFIX = "tag";

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
    public List<GiftCertificateDto> findAll() {
        List<GiftCertificateDto> certificates = giftCertificateService.findAll();
        certificates.forEach(this::setTagsToCertificate);
        return certificates;
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

    @Override
    public List<GiftCertificateDto> findGiftCertificatesByParams(Map<String, String> params,
                                                                 List<String> orderBy) {
        if (ObjectUtils.isNotEmpty(params) || ObjectUtils.isNotEmpty(orderBy)) {
            Map<String, String> tagProperties = params.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(TAG_PARAMS_PREFIX))
                    .map(entry -> Pair.of(
                            Introspector.decapitalize(entry.getKey().substring(TAG_PARAMS_PREFIX.length())),
                            entry.getValue()
                    ))
                    .filter(pair -> ReflectUtil.isContainsField(Tag.class,
                            ParamParseUtil.removeOperationCharacterIfPresent(pair.getLeft())
                    ))
                    .collect(toMap(Pair::getLeft, Pair::getRight));
            List<GiftCertificateDto> certificates = giftCertificateService.findByParams(params, tagProperties, orderBy);
            certificates.forEach(this::setTagsToCertificate);
            return certificates;
        } else {
            return findAll();
        }
    }

    private void setTagsToCertificate(GiftCertificateDto certificate) {
        List<TagDto> tags = tagService.findByGiftCertificate(certificate);
        certificate.tags().addAll(tags);
    }
}
