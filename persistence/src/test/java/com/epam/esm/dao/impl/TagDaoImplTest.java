package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DaoTestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@ActiveProfiles("test")
class TagDaoImplTest {

    private final TagDao tagDao;
    private final EntityManager entityManager;

    @Autowired
    TagDaoImplTest(TagDao tagDao, EntityManager entityManager) {
        this.tagDao = tagDao;
        this.entityManager = entityManager;
    }

    @Test
    @org.junit.jupiter.api.Tag("findByName")
    void shouldFindTagByName() {
        String tagName = "extreme";
        Tag tag = entityManager.createQuery("FROM Tag WHERE name = :name", Tag.class)
                .setParameter("name", tagName)
                .getSingleResult();
        Optional<Tag> expected = Optional.of(tag);

        Optional<Tag> actual = tagDao.findByName(tagName);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("findByName")
    void shouldReturnEmptyOptionalIfNoSuchName() {
        String noSuchName = "no such name";

        Optional<Tag> actual = tagDao.findByName(noSuchName);

        assertThat(actual).isEmpty();
    }

    @Test
    @org.junit.jupiter.api.Tag("createIfNotExists")
    void shouldCreateNewTag() {
        String tagName = "newTag";
        long count = entityManager.createQuery("FROM Tag WHERE name = :name", Tag.class)
                .setParameter("name", tagName)
                .getResultStream()
                .count();
        assertThat(count).isZero();

        Tag newTag = Tag.builder().name(tagName).build();
        tagDao.createIfNotExists(newTag);
        Long id = newTag.getId();

        assertThat(id).isNotNull();
    }

    @Test
    @org.junit.jupiter.api.Tag("createIfNotExists")
    void shouldReturnExistingTag() {
        String tagName = "extreme";
        Tag expected = entityManager.createQuery("FROM Tag WHERE name = :name", Tag.class)
                .setParameter("name", tagName)
                .getSingleResult();

        Tag newTag = Tag.builder().name(tagName).build();
        Tag actual = tagDao.createIfNotExists(newTag);
        Long id = newTag.getId();

        assertThat(id).isNull();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("findTopTagOfUserWithTheHighestCostOfAllOrders")
    void findTopTagOfUserWithTheHighestCostOfAllOrders() {
        Long id = 1L;
        Optional<Tag> expected = Optional.of(entityManager.find(Tag.class, id));

        Optional<Tag> actual = tagDao.findTopTagOfUserWithTheHighestCostOfAllOrders();

        assertThat(actual).isEqualTo(expected);
    }
}
