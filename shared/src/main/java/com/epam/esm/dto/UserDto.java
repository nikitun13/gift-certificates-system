package com.epam.esm.dto;

public record UserDto(Long id,
                      String email,
                      String firstName,
                      String lastName) {
}
