package com.epam.esm.dao;

import com.epam.esm.dto.GiftCertificateFilters;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @param filters  filtering args.
     * @param pageable pagination information.
     * @return list of the all {@code entities} from the repository.
     */
    Page<GiftCertificate> findAll(GiftCertificateFilters filters, Pageable pageable);
}
