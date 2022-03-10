package com.epam.esm.controller;

import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.constaints.GeneralConstraintsGroup;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final TagService tagService;
    private final PaginationUtil paginationUtil;

    public TagController(TagService tagService, PaginationUtil paginationUtil) {
        this.tagService = tagService;
        this.paginationUtil = paginationUtil;
    }

    @GetMapping
    public CollectionModel<?> findAll(Pageable pageable) {
        Page<?> page = tagService.findAll(pageable)
                .map(this::buildRepresentationModelWithSelfLink);

        List<?> content = page.getContent();
        PageMetadata metadata = paginationUtil.buildPageMetadata(page);
        Link selfLink = linkTo(methodOn(TagController.class)
                .findAll(pageable)).withSelfRel();
        Link topTagLink = linkTo(methodOn(TagController.class)
                .findTopTagOfUserWithTheHighestCostOfAllOrders())
                .withRel("topTag");
        return PagedModel.of(content, metadata, selfLink, topTagLink);
    }

    @GetMapping("/{id}")
    public RepresentationModel<?> findById(@PathVariable("id") Long id) {
        TagDto dto = tagService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return buildRepresentationModelWithSelfLink(dto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Validated(GeneralConstraintsGroup.class)
                                       @RequestBody CreateTagDto createDto) {
        TagDto dto = tagService.create(createDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").build(dto.id());
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean isDeleted = tagService.delete(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException(id);
    }

    @GetMapping("/top")
    public RepresentationModel<?> findTopTagOfUserWithTheHighestCostOfAllOrders() {
        TagDto dto = tagService.findTopTagOfUserWithTheHighestCostOfAllOrders()
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        Link selfLink = linkTo(methodOn(TagController.class)
                .findTopTagOfUserWithTheHighestCostOfAllOrders())
                .withSelfRel();
        Link tagLink = linkTo(methodOn(TagController.class)
                .findById(dto.id()))
                .withRel("tag");
        return RepresentationModel.of(dto, List.of(selfLink, tagLink));
    }

    private RepresentationModel<?> buildRepresentationModelWithSelfLink(TagDto tagDto) {
        Link link = linkTo(methodOn(TagController.class).findById(tagDto.id())).withSelfRel();
        return RepresentationModel.of(tagDto, List.of(link));
    }
}
