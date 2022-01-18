package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;

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
     */
    void update(UpdateGiftCertificateDto updateGiftCertificateDto);

    /**
     * Creates {@code GiftCertificate} entity. Creates many-to-many relationship
     * with new {@code Tags}. Not existing tags will be created.
     *
     * @param createGiftCertificateDto {@code dto} for creating {@code GiftCertificate}.
     */
    GiftCertificateDto create(UpdateGiftCertificateDto createGiftCertificateDto);
}
