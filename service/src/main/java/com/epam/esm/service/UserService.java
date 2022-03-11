package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

/**
 * Describes interface of the service that provides
 * CRUD operations with {@code User} entities.
 */
public interface UserService extends UserDetailsService {

    /**
     * Finds entities from the storage on the given page
     * and maps them to {@link UserDto}.
     *
     * @param pageable pagination information.
     * @return list of {@code UserDtos}.
     */
    Page<UserDto> findAll(Pageable pageable);

    /**
     * Finds entity by {@code id} and maps it to {@link UserDto}.
     * If no such {@code id} returns empty {@link Optional}.
     *
     * @param id {@code id} of the entity.
     * @return Optional {@code UserDto}.
     */
    Optional<UserDto> findById(Long id);
}
