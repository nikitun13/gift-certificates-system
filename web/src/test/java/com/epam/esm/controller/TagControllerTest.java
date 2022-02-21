package com.epam.esm.controller;

import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.util.PaginationUtil;
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
import org.springframework.test.web.servlet.MockMvc;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(PaginationUtil.class)
@WebMvcTest(TagController.class)
class TagControllerTest {

    private static final String TAGS_URI = "/tags";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TagService tagService;

    @Test
    @Tag("findAll")
    void shouldFindAllWithDefaultPage() throws Exception {
        TagDto first = new TagDto(1L, "first");
        TagDto second = new TagDto(2L, "second");
        TagDto third = new TagDto(3L, "third");
        PageRequest pageable = PageRequest.of(0, 20);
        Page<TagDto> page = new PageImpl<>(List.of(first, second, third), pageable, 3);
        doReturn(page)
                .when(tagService)
                .findAll(pageable);

        mvc.perform(get(TAGS_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.page.size", is(20)))
                .andExpect(jsonPath("$.page.totalElements", is(3)))
                .andExpect(jsonPath("$.page.totalPages", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$._embedded.tagDtoList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.tagDtoList[0].id", is(first.id()), Long.class))
                .andExpect(jsonPath("$._embedded.tagDtoList[0].name", is(first.name())));
    }

    @Test
    @Tag("findById")
    void shouldFindById() throws Exception {
        Long id = 2L;
        TagDto second = new TagDto(id, "second");
        doReturn(Optional.of(second))
                .when(tagService)
                .findById(id);

        mvc.perform(get(TAGS_URI + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id), Long.class))
                .andExpect(jsonPath("$.name", is(second.name())));
    }

    @Test
    @Tag("findById")
    void shouldReturn404IfNotFound() throws Exception {
        Long id = 200L;
        doReturn(Optional.empty())
                .when(tagService)
                .findById(id);

        mvc.perform(get(TAGS_URI + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(ENTITY_NOT_FOUND.getValue())));
    }

    @Test
    @Tag("create")
    void shouldCreateValidEntity() throws Exception {
        String tagName = "newTag";
        CreateTagDto createTagDto = new CreateTagDto(tagName);
        TagDto tagDto = new TagDto(5L, tagName);
        doReturn(tagDto)
                .when(tagService)
                .create(createTagDto);


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(createTagDto);

        mvc.perform(post(TAGS_URI).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", endsWith(TAGS_URI + "/" + tagDto.id())));
    }

    @Test
    @Tag("create")
    void shouldReturn400IfInvalidEntityReceived() throws Exception {
        String tagName = "n";
        CreateTagDto createTagDto = new CreateTagDto(tagName);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(createTagDto);

        mvc.perform(post(TAGS_URI).contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(METHOD_ARGUMENT_NOT_VALID.getValue())));
    }

    @Test
    @Tag("delete")
    void shouldReturn204ForExistingEntity() throws Exception {
        Long id = 2L;
        doReturn(true)
                .when(tagService)
                .delete(id);

        mvc.perform(delete(TAGS_URI + "/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @Tag("findById")
    void shouldReturn404IfNoSuchEntityForDelete() throws Exception {
        Long id = 200L;
        doReturn(false)
                .when(tagService)
                .delete(id);

        mvc.perform(get(TAGS_URI + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", is(ENTITY_NOT_FOUND.getValue())));
    }
}
