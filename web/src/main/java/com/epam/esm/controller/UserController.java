package com.epam.esm.controller;

import com.epam.esm.dto.DetailedOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.Page;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(@Valid Page page) {
        List<UserDto> result = userService.findAll(page);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") Long id) {
        UserDto dto = userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderDto>> findOrdersByUser(@PathVariable("id") Long id,
                                                           @Valid Page page) {
        UserDto dto = userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        List<OrderDto> result = orderService.findByUser(dto, page);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/orders/{orderId}")
    public ResponseEntity<DetailedOrderDto> findOrdersByUser(@PathVariable("id") Long id,
                                                           @PathVariable("orderId") Long orderId) {
        UserDto dto = userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        DetailedOrderDto result = orderService.findByUserAndId(dto, orderId)
                .orElseThrow(() -> new EntityNotFoundException(orderId));
        return ResponseEntity.ok(result);
    }
}
