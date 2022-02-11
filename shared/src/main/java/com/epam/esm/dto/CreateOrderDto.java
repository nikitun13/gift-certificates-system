package com.epam.esm.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public record CreateOrderDto(
        @NotEmpty(message = "order items can't be empty")
        List<@Valid CreateOrderDetailDto> details) {
}
