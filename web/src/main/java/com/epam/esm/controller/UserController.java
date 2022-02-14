package com.epam.esm.controller;

import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.DetailedOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final PaginationUtil paginationUtil;

    public UserController(UserService userService,
                          OrderService orderService,
                          PaginationUtil paginationUtil) {
        this.userService = userService;
        this.orderService = orderService;
        this.paginationUtil = paginationUtil;
    }

    @GetMapping
    public CollectionModel<?> findAll(Pageable pageable) {
        Page<?> page = userService.findAll(pageable)
                .map(this::buildRepresentationModelWithLinks);

        List<?> content = page.getContent();
        PageMetadata metadata = paginationUtil.buildPageMetadata(page);
        Link selfLink = linkTo(methodOn(UserController.class)
                .findAll(pageable))
                .withSelfRel();
        return PagedModel.of(content, metadata, selfLink);
    }

    @GetMapping("/{id}")
    public RepresentationModel<?> findById(@PathVariable("id") Long id) {
        UserDto dto = userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return buildRepresentationModelWithLinks(dto);
    }

    @GetMapping("/{id}/orders")
    public CollectionModel<?> findOrdersByUser(@PathVariable("id") Long id,
                                               Pageable pageable) {
        Page<?> page = orderService.findByUserId(id, pageable)
                .map(dto -> buildRepresentationModelWithSelfLink(dto, id));

        List<?> content = page.getContent();
        PageMetadata metadata = paginationUtil.buildPageMetadata(page);
        Link selfLink = linkTo(methodOn(UserController.class)
                .findOrdersByUser(id, pageable))
                .withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class)
                .findById(id))
                .withRel("user");
        return PagedModel.of(content, metadata, selfLink, userLink);
    }

    @GetMapping("/{id}/orders/{orderId}")
    public RepresentationModel<?> findDetailedOrderByUser(@PathVariable("id") Long id,
                                                          @PathVariable("orderId") Long orderId) {
        DetailedOrderDto result = orderService.findByUserIdAndId(id, orderId)
                .orElseThrow(() -> new EntityNotFoundException(orderId));

        Link selfLink = linkTo(methodOn(UserController.class)
                .findDetailedOrderByUser(id, orderId))
                .withSelfRel();
        return RepresentationModel.of(result, List.of(selfLink));
    }

    @PostMapping(value = "/{id}/orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createOrder(@PathVariable("id") Long id,
                                            @RequestBody @Valid CreateOrderDto createOrderDto) {
        DetailedOrderDto dto = orderService.create(createOrderDto, id);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").build(dto.id());
        return ResponseEntity.created(uri).build();
    }

    private RepresentationModel<?> buildRepresentationModelWithLinks(UserDto dto) {
        Link selfLink = linkTo(methodOn(UserController.class)
                .findById(dto.id()))
                .withSelfRel();
        Link ordersLink = linkTo(methodOn(UserController.class)
                .findOrdersByUser(dto.id(), Pageable.unpaged()))
                .withRel("orders");
        return RepresentationModel.of(dto, List.of(selfLink, ordersLink));
    }

    private RepresentationModel<?> buildRepresentationModelWithSelfLink(OrderDto dto, Long userId) {
        Link link = linkTo(methodOn(UserController.class)
                .findDetailedOrderByUser(userId, dto.id()))
                .withSelfRel();
        return RepresentationModel.of(dto, List.of(link));
    }
}
