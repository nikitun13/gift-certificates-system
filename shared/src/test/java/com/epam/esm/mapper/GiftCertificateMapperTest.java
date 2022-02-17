package com.epam.esm.mapper;

import com.epam.esm.MapperTestConfig;
import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateDtoWithoutTags;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = MapperTestConfig.class)
@ActiveProfiles("test")
class GiftCertificateMapperTest {

    @InjectMocks
    private GiftCertificateMapperImpl mapper;

    @Mock
    private TagMapper tagMapper;

    public static Stream<Arguments> giftCertificateToGiftCertificateDtoData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(new GiftCertificate(1L, "abc", "bca", 432L, 5, now, now,
                                List.of(new Tag(1L, "tag"), new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDto(1L, "abc", "bca", 432L, 5, now, now,
                                List.of(new TagDto(null, null), new TagDto(null, null), new TagDto(null, null)))),
                Arguments.of(new GiftCertificate(null, "abc", "bca", 432L, 5, now, now,
                                List.of(new Tag(1L, "tag"), new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDto(null, "abc", "bca", 432L, 5, now, now,
                                List.of(new TagDto(null, null), new TagDto(null, null), new TagDto(null, null)))),
                Arguments.of(new GiftCertificate(1L, null, "bca", 432L, 5, now, now,
                                List.of(new Tag(1L, "tag"), new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDto(1L, null, "bca", 432L, 5, now, now,
                                List.of(new TagDto(null, null), new TagDto(null, null), new TagDto(null, null)))),
                Arguments.of(new GiftCertificate(1L, "abc", null, 432L, 5, now, now,
                                List.of(new Tag(1L, "tag"), new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDto(1L, "abc", null, 432L, 5, now, now,
                                List.of(new TagDto(null, null), new TagDto(null, null), new TagDto(null, null)))),
                Arguments.of(new GiftCertificate(1L, "abc", "bca", null, 5, now, now,
                                List.of(new Tag(1L, "tag"), new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDto(1L, "abc", "bca", null, 5, now, now,
                                List.of(new TagDto(null, null), new TagDto(null, null), new TagDto(null, null)))),
                Arguments.of(new GiftCertificate(1L, "abc", "bca", 432L, null, now, now,
                                List.of(new Tag(1L, "tag"), new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDto(1L, "abc", "bca", 432L, null, now, now,
                                List.of(new TagDto(null, null), new TagDto(null, null), new TagDto(null, null)))),
                Arguments.of(new GiftCertificate(1L, "abc", "bca", 432L, 5, null, now,
                                List.of(new Tag(1L, "tag"), new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDto(1L, "abc", "bca", 432L, 5, null, now,
                                List.of(new TagDto(null, null), new TagDto(null, null), new TagDto(null, null)))),
                Arguments.of(new GiftCertificate(1L, "abc", "bca", 432L, 5, now, null,
                                List.of(new Tag(1L, "tag"), new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDto(1L, "abc", "bca", 432L, 5, now, null,
                                List.of(new TagDto(null, null), new TagDto(null, null), new TagDto(null, null)))),
                Arguments.of(new GiftCertificate(null, null, null, null, null, null, null,
                                Collections.emptyList()),
                        new GiftCertificateDto(null, null, null, null, null, null, null,
                                Collections.emptyList())),
                Arguments.of(new GiftCertificate(1L, "abc", "bca", 432L, 5, now, now,
                                List.of(new Tag(1L, "tag"))),
                        new GiftCertificateDto(1L, "abc", "bca", 432L, 5, now, now,
                                List.of(new TagDto(null, null))))
        );
    }

    public static Stream<Arguments> giftCertificateToGiftCertificateDtoWithoutTagsData() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(new GiftCertificate(1L, "abc", "bca", 432L, 5, now, now,
                                List.of(new Tag(1L, "tag"), new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDtoWithoutTags(1L, "abc", "bca", 432L, 5, now, now)),
                Arguments.of(new GiftCertificate(null, "abc", "bca", 432L, 5, now, now,
                                List.of(new Tag(1L, "tag"), new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDtoWithoutTags(null, "abc", "bca", 432L, 5, now, now)),
                Arguments.of(new GiftCertificate(1L, null, "bca", 432L, 5, now, now,
                                List.of(new Tag(1L, "tag"))),
                        new GiftCertificateDtoWithoutTags(1L, null, "bca", 432L, 5, now, now)),
                Arguments.of(new GiftCertificate(1L, "abc", null, 432L, 5, now, now,
                                List.of(new Tag(4L, "dummy"))),
                        new GiftCertificateDtoWithoutTags(1L, "abc", null, 432L, 5, now, now)),
                Arguments.of(new GiftCertificate(1L, "abc", "bca", null, 5, now, now,
                                List.of(new Tag())),
                        new GiftCertificateDtoWithoutTags(1L, "abc", "bca", null, 5, now, now)),
                Arguments.of(new GiftCertificate(1L, "abc", "bca", 432L, null, now, now,
                                List.of(new Tag(1L, "tag"), new Tag())),
                        new GiftCertificateDtoWithoutTags(1L, "abc", "bca", 432L, null, now, now)),
                Arguments.of(new GiftCertificate(1L, "abc", "bca", 432L, 5, null, now,
                                List.of(new Tag(), new Tag(4L, "dummy"))),
                        new GiftCertificateDtoWithoutTags(1L, "abc", "bca", 432L, 5, null, now)),
                Arguments.of(new GiftCertificate(1L, "abc", "bca", 432L, 5, now, null,
                                Collections.emptyList()),
                        new GiftCertificateDtoWithoutTags(1L, "abc", "bca", 432L, 5, now, null)),
                Arguments.of(new GiftCertificate(null, null, null, null, null, null, null,
                                Collections.emptyList()),
                        new GiftCertificateDtoWithoutTags(null, null, null, null, null, null, null)),
                Arguments.of(new GiftCertificate(1L, "abc", "bca", 432L, 5, now, now,
                                List.of(new Tag(1L, "tag"))),
                        new GiftCertificateDtoWithoutTags(1L, "abc", "bca", 432L, 5, now, now))
        );
    }

    public static Stream<Arguments> updateGiftCertificateDtoToGiftCertificateData() {
        return Stream.of(
                Arguments.of(new UpdateGiftCertificateDto("abc", "bca", 432L, 5,
                                List.of(new CreateTagDto("tag"), new CreateTagDto(null), new CreateTagDto("dummy"))),
                        new GiftCertificate(null, "abc", "bca", 432L, 5, null, null,
                                Collections.emptyList())),
                Arguments.of(new UpdateGiftCertificateDto(null, "bca", 432L, 5, Collections.emptyList()),
                        new GiftCertificate(null, null, "bca", 432L, 5, null, null,
                                Collections.emptyList())),
                Arguments.of(new UpdateGiftCertificateDto("abc", null, 432L, 5,
                                List.of(new CreateTagDto("tag"), new CreateTagDto(null))),
                        new GiftCertificate(null, "abc", null, 432L, 5, null, null,
                                Collections.emptyList())),
                Arguments.of(new UpdateGiftCertificateDto("abc", "bca", null, 5,
                                List.of(new CreateTagDto("tag"), new CreateTagDto("dummy"))),
                        new GiftCertificate(null, "abc", "bca", null, 5, null, null,
                                Collections.emptyList())),
                Arguments.of(new UpdateGiftCertificateDto("abc", "bca", 432L, null,
                                List.of(new CreateTagDto(null), new CreateTagDto("dummy"))),
                        new GiftCertificate(null, "abc", "bca", 432L, null, null, null,
                                Collections.emptyList())),
                Arguments.of(new UpdateGiftCertificateDto(null, null, null, null, Collections.emptyList()),
                        new GiftCertificate(null, null, null, null, null, null, null,
                                Collections.emptyList()))
        );
    }

    @ParameterizedTest
    @MethodSource("giftCertificateToGiftCertificateDtoData")
    @org.junit.jupiter.api.Tag("toGiftCertificateDto")
    void toGiftCertificateDto(GiftCertificate giftCertificate, GiftCertificateDto expected) {
        int numberOfInvocations = giftCertificate.getTags().size();
        doReturn(new TagDto(null, null))
                .when(tagMapper)
                .toTagDto(any());
        GiftCertificateDto actual = mapper.toGiftCertificateDto(giftCertificate);

        verify(tagMapper, times(numberOfInvocations))
                .toTagDto(any());
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("giftCertificateToGiftCertificateDtoWithoutTagsData")
    @org.junit.jupiter.api.Tag("toGiftCertificateDtoWithoutTags")
    void toGiftCertificateDtoWithoutTags(GiftCertificate giftCertificate, GiftCertificateDtoWithoutTags expected) {
        GiftCertificateDtoWithoutTags actual = mapper.toGiftCertificateDtoWithoutTags(giftCertificate);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("updateGiftCertificateDtoToGiftCertificateData")
    @org.junit.jupiter.api.Tag("toGiftCertificate")
    void toGiftCertificate(UpdateGiftCertificateDto updateGiftCertificateDto, GiftCertificate expected) {
        int numberOfInvocations = updateGiftCertificateDto.tags().size();
        doReturn(new Tag())
                .when(tagMapper)
                .toTag(any());
        GiftCertificate actual = mapper.toGiftCertificate(updateGiftCertificateDto);

        verify(tagMapper, times(numberOfInvocations))
                .toTag(any());
        assertThat(actual).isEqualTo(expected);
    }
}
