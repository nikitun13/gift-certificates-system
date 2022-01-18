package com.epam.esm.dto;

import com.epam.esm.dto.constaints.CreateGiftCertificateConstraintsGroup;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record CreateTagDto(
        @NotNull(groups = CreateGiftCertificateConstraintsGroup.class,
                message = "name can't be null")
        @Min(value = 2L, message = "name can't be less than 2 symbols")
        @Max(value = 128L, message = "name can't be more than 128 symbols")
        String name) {
}