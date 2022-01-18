package com.epam.esm.dto;

import com.epam.esm.dto.constaints.CreateGiftCertificateConstraintsGroup;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public record UpdateGiftCertificateDto(

        @NotNull(groups = CreateGiftCertificateConstraintsGroup.class,
                message = "id can't be null")
        @Positive(message = "id can't be negative")
        Long id,

        @NotNull(groups = CreateGiftCertificateConstraintsGroup.class,
                message = "name can't be null")
        @Min(value = 2L, message = "name can't be less than 2 symbols")
        @Max(value = 128L, message = "name can't be more than 128 symbols")
        String name,

        @NotNull(groups = CreateGiftCertificateConstraintsGroup.class,
                message = "description can't be null")
        @Min(value = 2L, message = "description can't be less than 2 symbols")
        String description,

        @NotNull(groups = CreateGiftCertificateConstraintsGroup.class,
                message = "price can't be null")
        @PositiveOrZero(message = "price can't be negative")
        Long price,

        @NotNull(groups = CreateGiftCertificateConstraintsGroup.class,
                message = "duration can't be null")
        @Min(value = 1L, message = "duration can't be less than 1")
        Integer duration,

        List<CreateTagDto> tags) {
}
