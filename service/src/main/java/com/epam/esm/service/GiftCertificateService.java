package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Describes interface of the service that provides
 * CRUD operations with {@code GiftCertificate} entities.
 */
public interface GiftCertificateService {

    /**
     * Finds all entities from the storage and maps them
     * to {@link GiftCertificateDto}.
     *
     * @return list of all {@code GiftCertificateDtos}.
     */
    List<GiftCertificateDto> findAll();

    /**
     * Finds entity by id and maps it to {@link GiftCertificateDto}.
     * If no such id returns empty {@link Optional}.
     *
     * @param id {@code id} of the entity.
     * @return Optional {@code GiftCertificateDto}.
     */
    Optional<GiftCertificateDto> findById(Long id);

    /**
     * Updates {@code GiftCertificate} in the storage.
     * All fields are optional, updates only required fields.
     *
     * @param updateGiftCertificateDto dto for update.
     * @param id                       {@code id} of the entity to be updated.
     * @return {@code true} if {@code entity} was updated successfully,
     * {@code false otherwise}.
     */
    boolean update(UpdateGiftCertificateDto updateGiftCertificateDto, Long id);

    /**
     * Deletes entity by its {@code id}.
     *
     * @param id {@code id} of the entity.
     * @return {@code true} if {@code entity} was deleted successfully,
     * {@code false otherwise}.
     */
    boolean delete(Long id);

    /**
     * Creates new {@code GiftCertificate} in the storage.
     * Uses {@link UpdateGiftCertificateDto} for creation,
     * all fields are required (except {@code id}).
     *
     * @param createGiftCertificateDto {@code dto} for creation new entity.
     * @return created entity that mapped to {@link GiftCertificateDto}.
     */
    GiftCertificateDto create(UpdateGiftCertificateDto createGiftCertificateDto);

    /**
     * Finds entities by different params including {@code Tag} params
     * and orders by the given fields. Unknown params in the
     * list of parameters will be ignored.
     *
     * @param params        filtering properties of the {@code certificate} (unknown properties will be ignored).
     * @param tagProperties filtering properties of the {@code tag}.
     * @param orderBy       list of the ordering by fields.
     * @return list of the dtos that satisfy all the filters.
     */
    List<GiftCertificateDto> findByParams(Map<String, String> params,
                                          Map<String, String> tagProperties,
                                          List<String> orderBy);
}
