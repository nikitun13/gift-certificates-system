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

    List<GiftCertificate> findByParams(Map<String, String> params, List<String> orderBy);
}
