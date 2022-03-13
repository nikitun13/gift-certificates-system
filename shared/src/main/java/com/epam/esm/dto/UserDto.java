package com.epam.esm.dto;

public record UserDto(Long id,
                      String username,
                      String firstName,
                      String lastName) {
}
