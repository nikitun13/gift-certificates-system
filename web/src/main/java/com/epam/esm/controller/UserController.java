package com.epam.esm.controller;

import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.CreateUserDto;
import com.epam.esm.dto.CustomUserDetails;
import com.epam.esm.dto.DetailedOrderDto;
import com.epam.esm.dto.LoginUserDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserAuthenticationService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.JwtUtil;
import com.epam.esm.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
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
@PreAuthorize("isAuthenticated()")
public class UserController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final UserService userService;
    private final UserAuthenticationService userAuthenticationService;
    private final OrderService orderService;
    private final PaginationUtil paginationUtil;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService,
                          UserAuthenticationService userAuthenticationService,
                          OrderService orderService,
                          PaginationUtil paginationUtil,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.userAuthenticationService = userAuthenticationService;
        this.orderService = orderService;
        this.paginationUtil = paginationUtil;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    @PreAuthorize("hasRole(T(com.epam.esm.entity.Role).ADMIN)")
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
    @PreAuthorize("hasRole(T(com.epam.esm.entity.Role).ADMIN) " +
            "or (isAuthenticated() and principal.id == #id)")
    public RepresentationModel<?> findById(@PathVariable("id") Long id) {
        UserDto dto = userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return buildRepresentationModelWithLinks(dto);
    }

    @GetMapping("/self")
    public RepresentationModel<?> findSelf(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return findById(userDetails.getId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAnonymous() or hasRole(T(com.epam.esm.entity.Role).ADMIN)")
    public ResponseEntity<Void> create(@RequestBody @Validated CreateUserDto createUserDto) {
        UserDto dto = userAuthenticationService.signUp(createUserDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").build(dto.id());
        return ResponseEntity.created(uri).build();
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAnonymous() or hasRole(T(com.epam.esm.entity.Role).ADMIN)")
    public ResponseEntity<Void> login(@RequestBody @Validated LoginUserDto loginUserDto) {
        UserDetails user = userAuthenticationService.login(loginUserDto);
        String jwt = jwtUtil.createJwt(user.getUsername());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwt)
                .build();
    }

    @GetMapping("/{id}/orders")
    @PreAuthorize("hasRole(T(com.epam.esm.entity.Role).ADMIN) " +
            "or ((hasRole(T(com.epam.esm.entity.Role).CLIENT) or hasAuthority('SCOPE_gcs.orders.read')) " +
            "and principal.id == #id)")
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
    @PreAuthorize("hasRole(T(com.epam.esm.entity.Role).ADMIN) " +
            "or ((hasRole(T(com.epam.esm.entity.Role).CLIENT) or hasAuthority('SCOPE_gcs.orders.read')) " +
            "and principal.id == #id)")
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
    @PreAuthorize("hasRole(T(com.epam.esm.entity.Role).ADMIN) " +
            "or ((hasRole(T(com.epam.esm.entity.Role).CLIENT) or hasAuthority('SCOPE_gcs.orders.write')) " +
            "and principal.id == #id)")
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
