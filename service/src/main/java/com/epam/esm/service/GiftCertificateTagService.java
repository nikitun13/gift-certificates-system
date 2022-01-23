package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
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
     * Finds all {@code GiftCertificates} with {@code Tags} from the storage.
     *
     * @return all {@code GiftCertificates} with {@code Tags}.
     */
    List<GiftCertificateDto> findAll();

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

    /**
     * Finds entities by different params and orders by the given fields.
     * Unknown params in the list of parameters will be ignored.
     * Sets {@code Tags} to the found {@code Certificates}.
     *
     * @param params  filtering properties of the {@code certificate} and {@code tag}.
     * @param orderBy list of the ordering by fields.
     * @return list of the {@link GiftCertificateDto} that satisfy all the filters
     * with the list of {@link TagDto}.
     */
    List<GiftCertificateDto> findGiftCertificatesByParams(Map<String, String> params, List<String> orderBy);
}
