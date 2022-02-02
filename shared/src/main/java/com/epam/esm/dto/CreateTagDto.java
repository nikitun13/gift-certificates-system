package com.epam.esm.dto;

import com.epam.esm.dto.constaints.GeneralConstraintsGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateTagDto(
        @Size(min = 2, max = 128,
                groups = GeneralConstraintsGroup.class,
                message = "name should be between 2 and 128 symbols")
        @NotBlank(groups = GeneralConstraintsGroup.class,
                message = "name can't be blank")
        String name) {
}
