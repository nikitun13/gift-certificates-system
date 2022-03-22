package com.epam.esm.dto;

public record CreateOktaUserDto(
        String email,
        String firstName,
        String lastName) {
}
