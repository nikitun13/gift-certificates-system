package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

/**
 * {@code GiftCertificateTagDao} provides {@code DAO} operations
 * with the {@code gift_certificate_tag} table in the database.
 * Represents many-to-many relationship between {@link GiftCertificate} and {@link Tag}.
 */
public interface GiftCertificateTagDao {

    /**
     * Creates many-to-many relationship.
     *
     * @param giftCertificateId id of the {@link GiftCertificate}.
     * @param tagId             id of the {@link Tag}.
     */
    void create(Long giftCertificateId, Long tagId);
}
