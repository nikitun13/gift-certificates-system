package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateFilters;
import com.epam.esm.dto.Page;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.dto.constaints.CreateGiftCertificateConstraintsGroup;
import com.epam.esm.dto.constaints.GeneralConstraintsGroup;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> findAll(GiftCertificateFilters filters,
                                                            @Valid Page page) {
        List<GiftCertificateDto> result = giftCertificateService.findAll(filters, page);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> findById(@PathVariable("id") Long id) {
        GiftCertificateDto dto = giftCertificateService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(
            @Validated({CreateGiftCertificateConstraintsGroup.class, GeneralConstraintsGroup.class})
            @RequestBody UpdateGiftCertificateDto createDto) {
        GiftCertificateDto dto = giftCertificateService.create(createDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").build(dto.id());
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean isDeleted = giftCertificateService.delete(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException(id);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@Validated(GeneralConstraintsGroup.class)
                                       @RequestBody UpdateGiftCertificateDto updateDto,
                                       @PathVariable("id") Long id) {
        boolean isUpdated = giftCertificateService.update(updateDto, id);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        throw new EntityNotFoundException(id);
    }
}
