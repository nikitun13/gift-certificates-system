package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoTestConfig;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = DaoTestConfig.class)
@ExtendWith(SpringExtension.class)
@Transactional
class TagDaoImplTest {

    @Autowired
    private TagDao tagDao;

    private final Tag cocaColaTag = new Tag(1L, "coca-cola");
    private final Tag kfcTag = new Tag(2L, "kfc");
    private final Tag moneyCertificateTag = new Tag(3L, "money certificate");
    private final Tag unusedTag = new Tag(4L, "unused tag");

    @Test
    @org.junit.jupiter.api.Tag("findAll")
    void shouldFindAllTagsFromDB() {
        List<Tag> expected = List.of(cocaColaTag, kfcTag, moneyCertificateTag, unusedTag);

        List<Tag> actual = tagDao.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("findById")
    void shouldFindTagById() {
        Optional<Tag> expected = Optional.of(moneyCertificateTag);

        Optional<Tag> actual = tagDao.findById(moneyCertificateTag.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("findById")
    void shouldReturnEmptyOptionalIfNoSuchId() {
        Optional<Tag> actual = tagDao.findById(500L);

        assertThat(actual).isEmpty();
    }

    @Test
    @org.junit.jupiter.api.Tag("create")
    void shouldSetIdToNewTag() {
        Tag newTag = Tag.builder().name("new tag").build();

        tagDao.create(newTag);

        assertThat(newTag.getId()).isNotNull();
    }

    @Test
    @org.junit.jupiter.api.Tag("create")
    void shouldCreateNewTagInDB() {
        Tag newTag = Tag.builder().name("new tag").build();

        tagDao.create(newTag);
        Optional<Tag> expected = Optional.of(newTag);
        Optional<Tag> actual = tagDao.findById(newTag.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("create")
    void shouldThrowDuplicateKeyExceptionIfSuchNameAlreadyExists() {
        Tag newTag = Tag.builder().name("kfc").build();

        assertThatThrownBy(() -> tagDao.create(newTag))
                .isExactlyInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @org.junit.jupiter.api.Tag("delete")
    void shouldDeleteTagById() {
        List<Tag> expected = List.of(cocaColaTag, moneyCertificateTag, unusedTag);

        tagDao.delete(kfcTag.getId());
        List<Tag> actual = tagDao.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("delete")
    void shouldNotDeleteAnythingIfNoSuchId() {
        List<Tag> expected = List.of(cocaColaTag, kfcTag, moneyCertificateTag, unusedTag);

        tagDao.delete(500L);
        List<Tag> actual = tagDao.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("findByName")
    void shouldFindTagByName() {
        Optional<Tag> expected = Optional.of(unusedTag);
        String name = unusedTag.getName();

        Optional<Tag> actual = tagDao.findByName(name);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @org.junit.jupiter.api.Tag("findByName")
    void shouldReturnEmptyOptionalIfNoSuchName() {
        String noSuchName = "no such name";

        Optional<Tag> actual = tagDao.findByName(noSuchName);

        assertThat(actual).isEmpty();
    }
}
