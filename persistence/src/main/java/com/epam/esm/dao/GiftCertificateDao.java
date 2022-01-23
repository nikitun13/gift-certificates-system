package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Map;

/**
 * The interface {@code GiftCertificateDao} is an interface that extends {@link BaseDao}
 * and provides new operations with {@link GiftCertificate} entities in the database.
 *
 * @see BaseDao
 * @see GiftCertificate
 */
public interface GiftCertificateDao extends BaseDao<Long, GiftCertificate> {

    /**
     * Finds entities by different params including {@code Tag} params
     * and orders by the given fields.
     *
     * @param certificateProperties filtering properties of the {@code certificate}.
     * @param tagProperties         filtering properties of the {@code tag}.
     * @param orderBy               list of the ordering by fields.
     * @return list of the entities that satisfy all the filters.
     */
    List<GiftCertificate> findByParams(Map<String, String> certificateProperties,
                                       Map<String, String> tagProperties,
                                       List<String> orderBy);
}
