package com.epam.esm.dto;

import com.epam.esm.dto.constaints.CreateGiftCertificateConstraintsGroup;
import com.epam.esm.dto.constaints.GeneralConstraintsGroup;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

public record UpdateGiftCertificateDto(
        @NotNull(groups = {CreateGiftCertificateConstraintsGroup.class},
                message = "name can't be null")
        @Size(min = 2, max = 128,
                groups = {GeneralConstraintsGroup.class, CreateGiftCertificateConstraintsGroup.class},
                message = "name should be between 2 and 128 symbols")
        String name,

        @NotNull(groups = {CreateGiftCertificateConstraintsGroup.class},
                message = "description can't be null")
        @Size(min = 2,
                groups = {GeneralConstraintsGroup.class, CreateGiftCertificateConstraintsGroup.class},
                message = "description can't be less than 2 symbols")
        String description,

        @NotNull(groups = {CreateGiftCertificateConstraintsGroup.class},
                message = "price can't be null")
        @PositiveOrZero(groups = {GeneralConstraintsGroup.class, CreateGiftCertificateConstraintsGroup.class},
                message = "price can't be negative")
        Long price,

        @NotNull(groups = CreateGiftCertificateConstraintsGroup.class,
                message = "duration can't be null")
        @Min(value = 1L,
                groups = {GeneralConstraintsGroup.class, CreateGiftCertificateConstraintsGroup.class},
                message = "duration can't be less than 1")
        Integer duration,

        List<@Valid CreateTagDto> tags) {
}
