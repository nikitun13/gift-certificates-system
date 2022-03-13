package com.epam.esm.service;

import com.epam.esm.dto.CreateUserDto;
import com.epam.esm.dto.LoginUserDto;
import com.epam.esm.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Describes interface of the service that provides
 * Authentication operations with {@code User} entities.
 */
public interface UserAuthenticationService {

    /**
     * Creates new {@code User} in the storage.
     *
     * @param createUserDto {@code dto} for creation new entity.
     * @return created entity that mapped to {@link UserDto}.
     */
    UserDto signUp(CreateUserDto createUserDto);

    /**
     * Authenticates {@code user} using {@link LoginUserDto}.
     *
     * @param loginUserDto {@code dto} that contains credentials.
     * @return {@link UserDetails} if credentials are valid.
     */
    UserDetails login(LoginUserDto loginUserDto);
}
