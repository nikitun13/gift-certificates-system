package com.epam.esm.controller;

import com.epam.esm.congif.ControllerTestConfiguration;
import com.epam.esm.dto.CreateOrderDetailDto;
import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.CreateUserDto;
import com.epam.esm.dto.DetailedOrderDto;
import com.epam.esm.dto.LoginUserDto;
import com.epam.esm.dto.OrderDetailDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDetailsDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserAuthenticationService;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.controller.handler.CustomStatus.BAD_CREDENTIALS;
import static com.epam.esm.controller.handler.CustomStatus.ENTITY_NOT_FOUND;
import static com.epam.esm.controller.handler.CustomStatus.METHOD_ARGUMENT_NOT_VALID;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ControllerTestConfiguration.class)
@WithMockUser(roles = "ADMIN")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserAuthenticationService userAuthenticationService;

    private static final String USERS_URI = "/users";
    private static final String USER_ORDERS_URI = USERS_URI + "/{id}/orders";

    @Test
    @Tag("findAll")
    void shouldReturn200ForFindAllWithDefaultPageable() throws Exception {
        UserDto first = new UserDto(1L, "first", "dummy", "dummy");
        UserDto second = new UserDto(2L, "second", "dummy", "dummy");
        UserDto third = new UserDto(3L, "third", "dummy", "dummy");
        PageRequest pageable = PageRequest.of(0, 20);
        Page<UserDto> page = new PageImpl<>(List.of(first, second, third), pageable, 3);
        doReturn(page)
                .when(userService)
                .findAll(pageable);

        mvc.perform(get(USERS_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.page.size", is(20)))
                .andExpect(jsonPath("$.page.totalElements", is(3)))
                .andExpect(jsonPath("$.page.totalPages", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$._embedded.userDtoList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.userDtoList[0].id", is(first.id()), Long.class))
                .andExpect(jsonPath("$._embedded.userDtoList[0].email", is(first.email())))
                .andExpect(jsonPath("$._links", not(nullValue())));
    }

    @Test
    @Tag("findById")
    void shouldReturnUserIfExists() throws Exception {
        Long id = 2L;
        UserDto second = new UserDto(2L, "second", "dummy", "dummy");
        doReturn(Optional.of(second))
                .when(userService)
                .findById(id);

        mvc.perform(get(USERS_URI + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id), Long.class))
                .andExpect(jsonPath("$.email", is(second.email())))
                .andExpect(jsonPath("$._links", not(nullValue())));
    }

    @Test
    @Tag("findById")
    void shouldReturn404IfNotFoundForFindById() throws Exception {
        Long id = 200L;
        doReturn(Optional.empty())
                .when(userService)
                .findById(id);

        mvc.perform(get(USERS_URI + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(ENTITY_NOT_FOUND.getValue())));
    }

    @Test
    @Tag("findOrdersByUser")
    void shouldReturnAllEntitiesWithDefaultPageable() throws Exception {
        Long id = 3L;
        LocalDateTime now = LocalDateTime.now();
        OrderDto first = new OrderDto(1L, now, now, 4354L);
        OrderDto second = new OrderDto(2L, now, now, 2345L);
        OrderDto third = new OrderDto(3L, now, now, 5635L);
        Pageable pageable = PageRequest.of(0, 20);
        Page<OrderDto> page = new PageImpl<>(List.of(first, second, third), pageable, 3);
        doReturn(page)
                .when(orderService)
                .findByUserId(id, pageable);
        mvc.perform(get(USER_ORDERS_URI, id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.page.size", is(20)))
                .andExpect(jsonPath("$.page.totalElements", is(3)))
                .andExpect(jsonPath("$.page.totalPages", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$._embedded.orderDtoList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.orderDtoList[0].id", is(first.id()), Long.class))
                .andExpect(jsonPath("$._embedded.orderDtoList[0].totalPrice", is(first.totalPrice()), Long.class))
                .andExpect(jsonPath("$._links", not(nullValue())));
    }

    @Test
    @Tag("findOrdersByUser")
    void shouldReturn200AndEmptyPageIfNoOrderByGivenUser() throws Exception {
        Long id = 4L;
        Pageable pageable = PageRequest.of(0, 20);
        Page<OrderDto> page = new PageImpl<>(List.of(), pageable, 0);
        doReturn(page)
                .when(orderService)
                .findByUserId(id, pageable);
        mvc.perform(get(USER_ORDERS_URI, id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.page.size", is(20)))
                .andExpect(jsonPath("$.page.totalElements", is(0)))
                .andExpect(jsonPath("$.page.totalPages", is(0)))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$._links", not(nullValue())));
    }

    @Test
    @Tag("findDetailedOrderByUser")
    void shouldReturn200IfOrderWithTheGivenIdByTheGivenUserExists() throws Exception {
        Long userId = 4L;
        Long orderId = 1L;
        OrderDetailDto firstDetail = new OrderDetailDto(1L, 250L, 4, null);
        OrderDetailDto secondDetail = new OrderDetailDto(2L, 500L, 2, null);
        List<OrderDetailDto> details = List.of(firstDetail, secondDetail);
        DetailedOrderDto order = new DetailedOrderDto(orderId, null, null, 567L, details);
        doReturn(Optional.of(order))
                .when(orderService)
                .findByUserIdAndId(userId, orderId);

        mvc.perform(get(USER_ORDERS_URI + "/{orderId}", userId, orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$._links", not(nullValue())))
                .andExpect(jsonPath("$.id", is(orderId), Long.class))
                .andExpect(jsonPath("$.totalPrice", is(order.totalPrice()), Long.class))
                .andExpect(jsonPath("$.details", hasSize(order.details().size())))
                .andExpect(jsonPath("$.details[0].price", is(250L), Long.class));
    }

    @Test
    @Tag("findDetailedOrderByUser")
    void shouldReturn404NoSuchOrderByTheGivenUser() throws Exception {
        Long userId = 400L;
        Long orderId = 1L;
        doReturn(Optional.empty())
                .when(orderService)
                .findByUserIdAndId(userId, orderId);

        mvc.perform(get(USER_ORDERS_URI + "/{orderId}", userId, orderId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(ENTITY_NOT_FOUND.getValue())));
    }

    @Test
    @Tag("createOrder")
    void shouldReturnCreatedIfValidEntityReceived() throws Exception {
        CreateOrderDetailDto first = new CreateOrderDetailDto(1L, 5);
        CreateOrderDetailDto second = new CreateOrderDetailDto(5L, 3);
        List<CreateOrderDetailDto> details = List.of(first, second);
        CreateOrderDto createOrderDto = new CreateOrderDto(details);
        Long userId = 3L;
        LocalDateTime now = LocalDateTime.now();
        OrderDetailDto firstDetail = new OrderDetailDto(3L, 250L, 5, null);
        OrderDetailDto secondDetail = new OrderDetailDto(5L, 500L, 3, null);
        List<OrderDetailDto> createdDetails = List.of(firstDetail, secondDetail);
        Long newOrderId = 7L;
        DetailedOrderDto createdOrder = new DetailedOrderDto(newOrderId, now, now, 800L, createdDetails);
        doReturn(createdOrder)
                .when(orderService)
                .create(createOrderDto, userId);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(createOrderDto);

        mvc.perform(post(USER_ORDERS_URI, userId).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", endsWith(USERS_URI + "/3/orders/" + newOrderId)));
    }

    @Test
    @Tag("createOrder")
    void shouldReturn400IfInvalidEntityReceivedForCreate() throws Exception {
        CreateOrderDetailDto first = new CreateOrderDetailDto(-1L, 5);
        CreateOrderDetailDto second = new CreateOrderDetailDto(5L, -3);
        List<CreateOrderDetailDto> details = List.of(first, second);
        CreateOrderDto createOrderDto = new CreateOrderDto(details);
        Long userId = 3L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(createOrderDto);

        mvc.perform(post(USER_ORDERS_URI, userId).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(METHOD_ARGUMENT_NOT_VALID.getValue())));
    }

    @Test
    @Tag("create")
    void shouldReturnCreatedForValidEntity() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto("ivan", "pass", "Ivan", "Ivanov");
        Long newId = 6L;
        UserDto dto = new UserDto(newId, createUserDto.email(), createUserDto.firstName(), createUserDto.lastName());
        doReturn(dto)
                .when(userAuthenticationService)
                .signUp(createUserDto);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(createUserDto);

        mvc.perform(post(USERS_URI).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, endsWith(USERS_URI + "/" + dto.id())));
    }

    @Test
    @Tag("create")
    void shouldReturn400IfInvalidEntityReceived() throws Exception {
        CreateUserDto invalidCreateUserDto = new CreateUserDto("iv", "pa", "", "Ivanov");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(invalidCreateUserDto);

        mvc.perform(post(USERS_URI).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(METHOD_ARGUMENT_NOT_VALID.getValue())));
    }

    @Test
    @Tag("login")
    void shouldReturn200ForValidUserCredentials() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto("ivan", "pass");
        Long id = 6L;
        UserDetails dto = new UserDetailsDto(id, loginUserDto.email(), "encodedPass", "Ivan", "Ivanov", Role.CLIENT);
        doReturn(dto)
                .when(userAuthenticationService)
                .login(loginUserDto);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(loginUserDto);

        mvc.perform(post(USERS_URI + "/login").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION));
    }

    @Test
    @Tag("login")
    void shouldReturn400IfInvalidLoginUserReceived() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto("iv", "p");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(loginUserDto);

        mvc.perform(post(USERS_URI + "/login").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(METHOD_ARGUMENT_NOT_VALID.getValue())));
    }

    @Test
    @Tag("login")
    void shouldReturn401IfBadUserCredentialsReceived() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto("ivan", "invalidPass");
        doThrow(BadCredentialsException.class)
                .when(userAuthenticationService)
                .login(loginUserDto);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(loginUserDto);

        mvc.perform(post(USERS_URI + "/login").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(BAD_CREDENTIALS.getValue())));
    }
}
