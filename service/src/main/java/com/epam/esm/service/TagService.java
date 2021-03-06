package com.epam.esm.service;

import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @param pageable pagination information.
     * @return list of all {@code TagDtos}.
     */
    Page<TagDto> findAll(Pageable pageable);

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
     * @return {@code true} if {@code entity} was deleted successfully,
     * {@code false otherwise}.
     */
    boolean delete(Long id);

    /**
     * Creates new {@code Tag} in the storage.
     *
     * @param createTagDto {@code dto} for creation new entity.
     * @return created entity that mapped to {@link TagDto}.
     */
    TagDto create(CreateTagDto createTagDto);

    /**
     * Finds entity by {@code name} and maps it to {@link TagDto}.
     * If no such {@code name} returns empty {@link Optional}.
     *
     * @param name {@code name} of the {@code Tag}.
     * @return Optional {@code TagDto}.
     */
    Optional<TagDto> findByName(String name);

    /**
     * Finds the most widely used tag of a user
     * with the highest cost of all orders.
     *
     * @return {@link Optional} {@link TagDto}.
     * Empty optional if nothing found.
     */
    Optional<TagDto> findTopTagOfUserWithTheHighestCostOfAllOrders();
}
