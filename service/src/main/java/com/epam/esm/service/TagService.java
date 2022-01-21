package com.epam.esm.service;

import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;

import java.util.List;
import java.util.Optional;

/**
 * Describes interface of the service that provides
 * CRUD operations with {@code Tag} entities.
 */
public interface TagService {

    /**
     * Finds all entities from the storage and maps them
     * to {@link TagDto}.
     *
     * @return list of all {@code TagDtos}.
     */
    List<TagDto> findAll();

    /**
     * Finds entity by {@code id} and maps it to {@link TagDto}.
     * If no such {@code id} returns empty {@link Optional}.
     *
     * @param id {@code id} of the entity.
     * @return Optional {@code TagDto}.
     */
    Optional<TagDto> findById(Long id);

    /**
     * Deletes entity by its {@code id}.
     *
     * @param id {@code id} of the entity.
     */
    void delete(Long id);

    /**
     * Creates new {@code Tag} in the storage.
     *
     * @param createTagDto {@code dto} for creation new entity.
     * @return created entity that mapped to {@link TagDto}.
     */
    TagDto create(CreateTagDto createTagDto);

    /**
     * Creates new {@code Tag} in the storage if not exists.
     * Returns new or existing {@code Tag} mapped to {@link TagDto}.
     *
     * @param createTagDto {@code dto} for creation new entity.
     * @return created entity or existing entity that mapped to {@link TagDto}.
     */
    TagDto createIfNotExists(CreateTagDto createTagDto);

    /**
     * Finds entity by {@code name} and maps it to {@link TagDto}.
     * If no such {@code name} returns empty {@link Optional}.
     *
     * @param name {@code name} of the {@code Tag}.
     * @return Optional {@code TagDto}.
     */
    Optional<TagDto> findByName(String name);

    /**
     * Finds all entities from the storage with the given {@link GiftCertificateDto}
     * and maps them to {@link TagDto}.
     *
     * @param giftCertificateDto {@code the certificates} whose {@code Tags} are needed to be received.
     * @return list of all {@code TagDtos} that have the given {@link GiftCertificateDto}.
     */
    List<TagDto> findByGiftCertificate(GiftCertificateDto giftCertificateDto);
}
