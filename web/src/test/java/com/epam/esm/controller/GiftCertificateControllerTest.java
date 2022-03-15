package com.epam.esm.controller;

import com.epam.esm.congif.ControllerTestConfiguration;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateFilters;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.controller.handler.CustomStatus.ENTITY_NOT_FOUND;
import static com.epam.esm.controller.handler.CustomStatus.METHOD_ARGUMENT_NOT_VALID;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ControllerTestConfiguration.class)
@WithMockUser(roles = "ADMIN")
@WebMvcTest(GiftCertificateController.class)
class GiftCertificateControllerTest {

    private static final String CERTIFICATES_URI = "/certificates";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GiftCertificateService service;

    @MockBean
    private UserService userService;

    @Test
    @Tag("findAll")
    void shouldReturnAllEntitiesWithDefaultPageable() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        GiftCertificateDto first = new GiftCertificateDto(
                1L, "First", "desc1", 500L, 5, now, now, Collections.emptyList());
        GiftCertificateDto second = new GiftCertificateDto(
                2L, "Second", "desc2", 1000L, 7, now, now, Collections.emptyList());
        GiftCertificateDto third = new GiftCertificateDto(
                3L, "Third", "desc3", 1500L, 3, now, now, Collections.emptyList());
        Pageable pageable = PageRequest.of(0, 20);
        Page<GiftCertificateDto> page = new PageImpl<>(List.of(first, second, third), pageable, 3);
        doReturn(page)
                .when(service)
                .findAll(new GiftCertificateFilters(null, null, null, null), pageable);

        mvc.perform(get(CERTIFICATES_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.page.size", is(20)))
                .andExpect(jsonPath("$.page.totalElements", is(3)))
                .andExpect(jsonPath("$.page.totalPages", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$._embedded.giftCertificateDtoList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.giftCertificateDtoList[0].id", is(first.id()), Long.class))
                .andExpect(jsonPath("$._embedded.giftCertificateDtoList[0].price", is(first.price()), Long.class))
                .andExpect(jsonPath("$._embedded.giftCertificateDtoList[0].description", is(first.description())))
                .andExpect(jsonPath("$._embedded.giftCertificateDtoList[0].duration", is(first.duration())))
                .andExpect(jsonPath("$._embedded.giftCertificateDtoList[0].name", is(first.name())));
    }

    @Test
    @Tag("findById")
    void shouldReturnCertificateIfExists() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Long id = 2L;
        GiftCertificateDto second = new GiftCertificateDto(
                id, "Second", "desc2", 1000L, 7, now, now, Collections.emptyList());
        doReturn(Optional.of(second))
                .when(service)
                .findById(id);

        mvc.perform(get(CERTIFICATES_URI + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id), Long.class))
                .andExpect(jsonPath("$.name", is(second.name())));
    }

    @Test
    @Tag("findById")
    void shouldReturn404IfNotFoundForFindById() throws Exception {
        Long id = 200L;
        doReturn(Optional.empty())
                .when(service)
                .findById(id);

        mvc.perform(get(CERTIFICATES_URI + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(ENTITY_NOT_FOUND.getValue())));
    }

    @Test
    @Tag("create")
    void shouldReturnCreatedForValidEntity() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        UpdateGiftCertificateDto createDto = new UpdateGiftCertificateDto(
                "new certificate", "desc", 500L, 4, Collections.emptyList());
        Long newId = 6L;
        GiftCertificateDto dto = new GiftCertificateDto(newId, createDto.name(), createDto.description(),
                createDto.price(), createDto.duration(), now, now, Collections.emptyList());
        doReturn(dto)
                .when(service)
                .create(createDto);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(createDto);

        mvc.perform(post(CERTIFICATES_URI).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", endsWith(CERTIFICATES_URI + "/" + dto.id())));
    }

    @Test
    @Tag("create")
    void shouldReturn400IfInvalidEntityReceived() throws Exception {
        UpdateGiftCertificateDto createDto = new UpdateGiftCertificateDto(
                "new certificate", "desc", -500L, -4, Collections.emptyList());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(createDto);

        mvc.perform(post(CERTIFICATES_URI).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(METHOD_ARGUMENT_NOT_VALID.getValue())));
    }

    @Test
    @Tag("delete")
    void shouldReturn204ForExistingEntity() throws Exception {
        Long id = 2L;
        doReturn(true)
                .when(service)
                .delete(id);

        mvc.perform(delete(CERTIFICATES_URI + "/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @Tag("delete")
    void shouldReturn404IfNoSuchEntityForDelete() throws Exception {
        Long id = 200L;
        doReturn(false)
                .when(service)
                .delete(id);

        mvc.perform(delete(CERTIFICATES_URI + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(ENTITY_NOT_FOUND.getValue())));
    }

    @Test
    @Tag("update")
    void shouldReturn200IfValidIdAndEntityReceived() throws Exception {
        UpdateGiftCertificateDto updateDto = new UpdateGiftCertificateDto(
                null, null, 500L, 4, Collections.emptyList());
        Long id = 3L;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(updateDto);
        doReturn(true)
                .when(service)
                .update(updateDto, id);

        mvc.perform(patch(CERTIFICATES_URI + "/{id}", id).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("update")
    void shouldReturn4o4IfNoSuchIdForUpdate() throws Exception {
        UpdateGiftCertificateDto updateDto = new UpdateGiftCertificateDto(
                "new certificate", "desc", 500L, 4, Collections.emptyList());
        Long id = 300L;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(updateDto);
        doReturn(false)
                .when(service)
                .update(updateDto, id);

        mvc.perform(patch(CERTIFICATES_URI + "/{id}", id).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(ENTITY_NOT_FOUND.getValue())));
    }

    @Test
    @Tag("update")
    void shouldReturn400IfInvalidEntityReceivedForUpdate() throws Exception {
        Long id = 3L;
        UpdateGiftCertificateDto updateDto = new UpdateGiftCertificateDto(
                "new certificate", "c", -500L, 4, Collections.emptyList());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(updateDto);

        mvc.perform(patch(CERTIFICATES_URI + "/{id}", id).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(METHOD_ARGUMENT_NOT_VALID.getValue())));
    }
}
