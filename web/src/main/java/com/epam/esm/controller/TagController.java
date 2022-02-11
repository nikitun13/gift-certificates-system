package com.epam.esm.controller;

import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.Page;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.constaints.GeneralConstraintsGroup;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.TagService;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> findAll(@Valid Page page) {
        List<TagDto> result = tagService.findAll(page);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findById(@PathVariable("id") Long id) {
        TagDto dto = tagService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return ResponseEntity.ok(dto);
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
    public ResponseEntity<TagDto> findTopTagOfUserWithTheHighestCostOfAllOrders() {
        Optional<TagDto> maybeTopTag = tagService.findTopTagOfUserWithTheHighestCostOfAllOrders();
        return ResponseEntity.of(maybeTopTag);
    }
}
