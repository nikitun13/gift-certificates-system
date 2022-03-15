package com.epam.esm.mapper;

import com.epam.esm.MapperTestConfig;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Tag;
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
class UserMapperTest {

    private final UserMapper mapper;

    @Autowired
    UserMapperTest(UserMapper mapper) {
        this.mapper = mapper;
    }

    public static Stream<Arguments> userToUserDtoData() {
        return Stream.of(
                Arguments.of(User.builder().build(), new UserDto(null, null, null, null)),
                Arguments.of(User.builder().id(1L).build(), new UserDto(1L, null, null, null)),
                Arguments.of(User.builder().username("dummy").build(), new UserDto(null, "dummy", null, null)),
                Arguments.of(User.builder().id(1L).username("dummy").build(), new UserDto(1L, "dummy", null, null))
        );
    }

    @ParameterizedTest
    @MethodSource("userToUserDtoData")
    @Tag("toUserDto")
    void toUserDto(User user, UserDto expected) {
        UserDto actual = mapper.toUserDto(user);

        assertThat(actual).isEqualTo(expected);
    }
}
