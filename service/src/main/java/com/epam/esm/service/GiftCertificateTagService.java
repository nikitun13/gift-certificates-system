package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;

import java.util.List;
import java.util.Map;

/**
 * Describes interface of the service that provides transactional
 * operations with {@code GiftCertificate} and {@code Tag} entities.
 *
 * @see GiftCertificateService
 * @see TagService
 */
public interface GiftCertificateTagService {

    /**
     * Updates {@code GiftCertificate}. Creates many-to-many relationship
     * with new {@code Tags}. Not existing tags will be created.
     *
     * @param updateGiftCertificateDto {@code dto} for updating {@code GiftCertificate}.
     * @param giftCertificateId        {@code id} of the {@code GiftCertificate}.
     */
    void update(UpdateGiftCertificateDto updateGiftCertificateDto, Long giftCertificateId);

    /**
     * Creates {@code GiftCertificate} entity. Creates many-to-many relationship
     * with new {@code Tags}. Not existing tags will be created.
     *
     * @param createGiftCertificateDto {@code dto} for creating {@code GiftCertificate}.
     */
    GiftCertificateDto create(UpdateGiftCertificateDto createGiftCertificateDto);

    List<GiftCertificateDto> findGiftCertificatesByParams(Map<String, String> params, List<String> orderBy);
}
