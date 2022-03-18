package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OktaUserDto(
        @JsonProperty("active")
        boolean isActive,

        @JsonProperty("sub")
        String email,

        String firstName,
        String lastName,
        String scope) {
}
