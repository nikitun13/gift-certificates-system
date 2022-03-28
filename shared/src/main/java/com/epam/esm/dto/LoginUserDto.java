package com.epam.esm.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record LoginUserDto(
        @NotNull(message = "email can't be null")
        @Email
        String email,

        @NotNull(message = "password can't be null")
        @Size(min = 4, max = 256,
                message = "password should be between 4 and 256 symbols")
        String password) {
}
