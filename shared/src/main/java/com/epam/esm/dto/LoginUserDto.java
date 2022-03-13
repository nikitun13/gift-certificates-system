package com.epam.esm.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record LoginUserDto(
        @NotNull(message = "username can't be null")
        @Size(min = 4, max = 128,
                message = "username should be between 4 and 128 symbols")
        String username,

        @NotNull(message = "password can't be null")
        @Size(min = 4, max = 256,
                message = "password should be between 4 and 256 symbols")
        String password) {
}
