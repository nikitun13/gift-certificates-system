package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DaoTestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@ActiveProfiles("test")
class OrderDaoImplTest {

    private static final PageRequest DEFAULT_PAGEABLE = PageRequest.of(0, 20);

    private final OrderDao orderDao;
    private final EntityManager entityManager;

    @Autowired
    OrderDaoImplTest(OrderDao orderDao, EntityManager entityManager) {
        this.orderDao = orderDao;
        this.entityManager = entityManager;
    }

    public static Stream<Arguments> dataForCountByUser() {
        return Stream.of(
                Arguments.of(1L, 3L),
                Arguments.of(4L, 0L),
                Arguments.of(400L, 0L)
        );
    }

    @Test
    @Tag("findByUser")
    void shouldFindOrdersByTheGivenUser() {
        Long userId = 1L;
        User user = new User(userId, "dummy", "dummy", "dummy", "dummy", Role.CLIENT);
        List<Order> list = entityManager.createQuery("FROM Order WHERE user = :user", Order.class)
                .setParameter("user", user)
                .getResultList();
        Page<Order> expected = new PageImpl<>(list, DEFAULT_PAGEABLE, list.size());

        Page<Order> actual = orderDao.findByUser(user, DEFAULT_PAGEABLE);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByUser")
    void shouldReturnEmptyPageIfNoSuchUser() {
        Long userId = 100L;
        User user = new User(userId, "dummy", "dummy", "dummy", "dummy", Role.CLIENT);
        Page<Order> expected = new PageImpl<>(Collections.emptyList(), DEFAULT_PAGEABLE, 0);

        Page<Order> actual = orderDao.findByUser(user, DEFAULT_PAGEABLE);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByUser")
    void shouldReturnEmptyPageOrdersByTheGivenUser() {
        Long userId = 4L;
        User user = new User(userId, "dummy", "dummy", "dummy", "dummy", Role.CLIENT);

        Page<Order> expected = new PageImpl<>(Collections.emptyList(), DEFAULT_PAGEABLE, 0);

        Page<Order> actual = orderDao.findByUser(user, DEFAULT_PAGEABLE);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByUserAndId")
    void shouldReturnOrderByItsIdAndUser() {
        Long id = 1L;
        Long userId = 1L;
        User user = new User(userId, "dummy", "dummy", "dummy", "dummy", Role.CLIENT);

        Order order = entityManager.createQuery("FROM Order WHERE user = :user and id = :id", Order.class)
                .setParameter("user", user)
                .setParameter("id", id)
                .getSingleResult();
        Optional<Order> expected = Optional.of(order);

        Optional<Order> actual = orderDao.findByUserAndId(user, id);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByUserAndId")
    void shouldReturnEmptyOptionalIfNoSuchOrderIdByTheGivenUser() {
        Long id = 2L;
        Long userId = 1L;
        User user = new User(userId, "dummy", "dummy", "dummy", "dummy", Role.CLIENT);

        Optional<Order> actual = orderDao.findByUserAndId(user, id);

        assertThat(actual).isEmpty();
    }

    @Test
    @Tag("findByUserAndId")
    void shouldReturnEmptyOptionalIfNoSuchUser() {
        Long id = 2L;
        Long userId = 100L;
        User user = new User(userId, "dummy", "dummy", "dummy", "dummy", Role.CLIENT);

        Optional<Order> actual = orderDao.findByUserAndId(user, id);

        assertThat(actual).isEmpty();
    }

    @Test
    @Tag("findByUserAndId")
    void shouldReturnEmptyOptionalIfNoSuchUserAndOrderId() {
        Long id = 200L;
        Long userId = 100L;
        User user = new User(userId, "dummy", "dummy", "dummy", "dummy", Role.CLIENT);

        Optional<Order> actual = orderDao.findByUserAndId(user, id);

        assertThat(actual).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("dataForCountByUser")
    @Tag("countByUser")
    void shouldCountOrderByTheGivenUser(Long userId, long expected) {
        User user = new User(userId, "dummy", "dummy", "dummy", "dummy", Role.CLIENT);

        long actual = orderDao.countByUser(user);

        assertThat(actual).isEqualTo(expected);
    }
}
