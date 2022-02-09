package com.epam.esm.dto;

import java.util.List;

public record UserDto(Long id,
                      String username,
                      List<UserOrderDto> orders) {
}
