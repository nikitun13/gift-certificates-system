package com.epam.esm.mapper;

import com.epam.esm.MapperTestConfig;
import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MapperTestConfig.class)
@ActiveProfiles("test")
class TagMapperTest {

    private final TagMapper mapper;

    @Autowired
    TagMapperTest(TagMapper mapper) {
        this.mapper = mapper;
    }

    public static Stream<Arguments> createTagDtoToTagData() {
        return Stream.of(
                Arguments.of(new CreateTagDto("dummy"), new Tag(null, "dummy")),
                Arguments.of(new CreateTagDto(null), new Tag())
        );
    }

    public static Stream<Arguments> TagToTagDtoData() {
        return Stream.of(
                Arguments.of(new Tag(1L, "dummy"), new TagDto(1L, "dummy")),
                Arguments.of(new Tag(null, "dummy"), new TagDto(null, "dummy")),
                Arguments.of(new Tag(), new TagDto(null, null)),
                Arguments.of(new Tag(1L, null), new TagDto(1L, null))
        );
    }

    @ParameterizedTest
    @MethodSource("createTagDtoToTagData")
    @org.junit.jupiter.api.Tag("toTag")
    void toTag(CreateTagDto dto, Tag expected) {
        Tag actual = mapper.toTag(dto);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("TagToTagDtoData")
    @org.junit.jupiter.api.Tag("toTagDto")
    void toTagDto(Tag tag, TagDto expected) {
        TagDto actual = mapper.toTagDto(tag);

        assertThat(actual).isEqualTo(expected);
    }
}
