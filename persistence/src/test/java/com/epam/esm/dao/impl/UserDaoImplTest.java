package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DaoTestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@ActiveProfiles("test")
class UserDaoImplTest {

    private final UserDao userDao;

    @Autowired
    UserDaoImplTest(UserDao userDao) {
        this.userDao = userDao;
    }

    @Test
    @Tag("findByUsername")
    void shouldFindUserByExistingUsername() {
        String username = "nick";
        User user = User.builder()
                .id(3L)
                .email(username)
                .password("$2a$10$t7Yelc6KO8lOcOQUd1eYDOl/T6LxVPWwee4DlMTSdNOrSo.JZ9cNq")
                .firstName("Nick")
                .lastName("Third")
                .role(Role.CLIENT)
                .build();
        Optional<User> expected = Optional.of(user);

        Optional<User> actual = userDao.findByEmail(username);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByUsername")
    void shouldReturnEmptyOptionalIfNoSuchUsername() {
        String username = "noSuchUsername";

        Optional<User> actual = userDao.findByEmail(username);

        assertThat(actual).isEmpty();
    }
}
