package com.epam.esm.service;

import com.epam.esm.dto.Page;
import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Optional;

/**
 * Describes interface of the service that provides
 * CRUD operations with {@code User} entities.
 */
public interface UserService {

    /**
     * Finds entities from the storage on the given page
     * and maps them to {@link UserDto}.
     *
     * @param page current page.
     * @return list of {@code UserDtos}.
     */
    List<UserDto> findAll(Page page);

    /**
     * Finds entity by {@code id} and maps it to {@link UserDto}.
     * If no such {@code id} returns empty {@link Optional}.
     *
     * @param id {@code id} of the entity.
     * @return Optional {@code UserDto}.
     */
    Optional<UserDto> findById(Long id);
}
