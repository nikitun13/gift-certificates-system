package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateFilters;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.dto.constaints.CreateGiftCertificateConstraintsGroup;
import com.epam.esm.dto.constaints.GeneralConstraintsGroup;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("isAuthenticated()")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final PaginationUtil paginationUtil;

    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     PaginationUtil paginationUtil) {
        this.giftCertificateService = giftCertificateService;
        this.paginationUtil = paginationUtil;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public CollectionModel<?> findAll(GiftCertificateFilters filters,
                                      Pageable pageable) {
        Page<?> page = giftCertificateService.findAll(filters, pageable)
                .map(this::buildRepresentationModelWithSelfLink);

        List<?> content = page.getContent();
        PageMetadata metadata = paginationUtil.buildPageMetadata(page);
        Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                .findAll(filters, pageable))
                .withSelfRel();
        return PagedModel.of(content, metadata, selfLink);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public RepresentationModel<?> findById(@PathVariable("id") Long id) {
        GiftCertificateDto dto = giftCertificateService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return buildRepresentationModelWithSelfLink(dto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(T(com.epam.esm.entity.Role).ADMIN)")
    public ResponseEntity<Void> create(
            @Validated({CreateGiftCertificateConstraintsGroup.class, GeneralConstraintsGroup.class})
            @RequestBody UpdateGiftCertificateDto createDto) {
        GiftCertificateDto dto = giftCertificateService.create(createDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").build(dto.id());
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(com.epam.esm.entity.Role).ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean isDeleted = giftCertificateService.delete(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException(id);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(T(com.epam.esm.entity.Role).ADMIN)")
    public ResponseEntity<Void> update(@Validated(GeneralConstraintsGroup.class)
                                       @RequestBody UpdateGiftCertificateDto updateDto,
                                       @PathVariable("id") Long id) {
        boolean isUpdated = giftCertificateService.update(updateDto, id);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        throw new EntityNotFoundException(id);
    }

    private RepresentationModel<?> buildRepresentationModelWithSelfLink(GiftCertificateDto dto) {
        Link link = linkTo(methodOn(GiftCertificateController.class).findById(dto.id())).withSelfRel();
        return RepresentationModel.of(dto, List.of(link));
    }
}
