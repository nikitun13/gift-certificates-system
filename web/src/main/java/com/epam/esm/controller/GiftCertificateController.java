package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.dto.constaints.CreateGiftCertificateConstraintsGroup;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> findAll(
            @RequestParam(required = false) Map<String, String> filters,
            @RequestParam(defaultValue = "") List<String> orderBy) {
        List<GiftCertificateDto> result = giftCertificateTagService.findGiftCertificatesByParams(filters, orderBy);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> findById(@PathVariable("id") Long id) {
        Optional<GiftCertificateDto> maybeDto = giftCertificateTagService.findById(id);
        return ResponseEntity.of(maybeDto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Validated(CreateGiftCertificateConstraintsGroup.class)
    public ResponseEntity<Void> create(@RequestBody UpdateGiftCertificateDto createDto) {
        GiftCertificateDto dto = giftCertificateTagService.create(createDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").build(dto.id());
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Valid UpdateGiftCertificateDto updateDto,
                                       @PathVariable("id") Long id) {
        giftCertificateTagService.update(updateDto, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
