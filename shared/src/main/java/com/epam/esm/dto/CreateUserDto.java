package com.epam.esm.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record CreateUserDto(
        @NotNull(message = "username can't be null")
        @Size(min = 4, max = 128,
                message = "username should be between 4 and 128 symbols")
        String username,

        @NotNull(message = "password can't be null")
        @Size(min = 4, max = 256,
                message = "password should be between 4 and 256 symbols")
        String password,

        @NotNull(message = "firstName can't be null")
        @Size(min = 2, max = 256,
                message = "firstName should be between 2 and 256 symbols")
        String firstName,

        @NotNull(message = "lastName can't be null")
        @Size(min = 2, max = 256,
                message = "lastName should be between 2 and 256 symbols")
        String lastName) {
}
