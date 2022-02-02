package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.dto.constaints.CreateGiftCertificateConstraintsGroup;
import com.epam.esm.dto.constaints.GeneralConstraintsGroup;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateTagService giftCertificateTagService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     GiftCertificateTagService giftCertificateTagService) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateTagService = giftCertificateTagService;
    }

    /**
     * Finds entities with filters. If no filters provided, finds all entities.
     * Also orders found entities by the given field if ascending or descending orders.<br/>
     * The fields by which to sort should come under the parameter name {@code 'orderBy'}.
     * By default, it uses ascending ordering, append {@code '-'} to the beginning of the field name
     * to use descending ordering.<br/>
     * Filters should come according to the following convention:
     * <ul>
     *     <li>Field name should be as a parameter key</li>
     *     <li>
     *         Tags params should start with {@code 'Tag'} prefix
     *         and {@code Camel case} should be observed (e.g. {@code tagName})
     *     </li>
     *     <li>All are combined filters are combined by logical {@code AND}</li>
     *     <li>Currently supported only {@code String} fields</li>
     *     <li>
     *         By default, it find exact match with param value.
     *         Append {@code '~'} to the end of the parameter name (fieldName) to
     *         find by part of the given value (matches if contains the given value).
     *         E.g. /certificates?name~=sky - finds certificates which names contains {@code 'sky'}
     *     </li>
     * </ul>
     *
     * @param filters for searching through entities.
     * @param orderBy for ordering found entities.
     * @return list of GiftCertificateDto mapped to JSON value.
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> findAll(
            @RequestParam(required = false) Map<String, String> filters,
            @RequestParam(defaultValue = "") List<String> orderBy) {
        List<GiftCertificateDto> result = giftCertificateTagService.findGiftCertificatesByParams(filters, orderBy);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> findById(@PathVariable("id") Long id) {
        GiftCertificateDto dto = giftCertificateTagService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(
            @Validated({CreateGiftCertificateConstraintsGroup.class, GeneralConstraintsGroup.class})
            @RequestBody UpdateGiftCertificateDto createDto) {
        GiftCertificateDto dto = giftCertificateTagService.create(createDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").build(dto.id());
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean isDeleted = giftCertificateService.delete(id);
        if (BooleanUtils.isFalse(isDeleted)) {
            throw new EntityNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@Validated(GeneralConstraintsGroup.class)
                                       @RequestBody UpdateGiftCertificateDto updateDto,
                                       @PathVariable("id") Long id) {
        boolean isUpdated = giftCertificateTagService.update(updateDto, id);
        if (BooleanUtils.isFalse(isUpdated)) {
            throw new EntityNotFoundException(id);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
