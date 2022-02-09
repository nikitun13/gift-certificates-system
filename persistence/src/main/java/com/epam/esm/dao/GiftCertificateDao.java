package com.epam.esm.dao;

import com.epam.esm.dto.GiftCertificateFilters;
import com.epam.esm.dto.Page;
import com.epam.esm.entity.GiftCertificate;

import java.util.List;

/**
 * The interface {@code GiftCertificateDao} is an interface that extends {@link BaseDao}
 * and provides new operations with {@link GiftCertificate} entities in the database.
 *
 * @see BaseDao
 * @see GiftCertificate
 */
public interface GiftCertificateDao extends BaseDao<Long, GiftCertificate> {

    /**
     * Returns all {@code entities} from the repository
     * on the current page that matches filers.
     *
     * @param filters filtering args.
     * @param page    current page.
     * @return list of the all {@code entities} from the repository.
     */
    List<GiftCertificate> findAll(GiftCertificateFilters filters, Page page);
}
