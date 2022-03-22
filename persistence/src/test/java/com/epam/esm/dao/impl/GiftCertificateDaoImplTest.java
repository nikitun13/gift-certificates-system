package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.dto.GiftCertificateFilters;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DaoTestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@ActiveProfiles("test")
class GiftCertificateDaoImplTest {

    private static final PageRequest DEFAULT_PAGEABLE = PageRequest.of(0, 20);

    private final GiftCertificateDao giftCertificateDao;
    private final EntityManager entityManager;

    @Autowired
    GiftCertificateDaoImplTest(GiftCertificateDao giftCertificateDao, EntityManager entityManager) {
        this.giftCertificateDao = giftCertificateDao;
        this.entityManager = entityManager;
    }

    private long countTotalEntities() {
        return entityManager.createQuery("SELECT count(gc) FROM GiftCertificate gc", Long.class)
                .getSingleResult();
    }

    @Test
    @Tag("findAll")
    void shouldFindAllCertificates() {
        long totalEntities = countTotalEntities();
        List<GiftCertificate> list = entityManager.createQuery("FROM GiftCertificate", GiftCertificate.class)
                .getResultList();
        Page<GiftCertificate> expected = new PageImpl<>(list, DEFAULT_PAGEABLE, totalEntities);

        Page<GiftCertificate> actual = giftCertificateDao.findAll(DEFAULT_PAGEABLE);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findAll")
    void shouldFindCertificatesWithLimitAndOffset() {
        long totalEntities = countTotalEntities();
        List<GiftCertificate> list = entityManager.createQuery("FROM GiftCertificate", GiftCertificate.class)
                .setFirstResult(2)
                .setMaxResults(2)
                .getResultList();
        PageRequest pageable = PageRequest.of(1, 2);
        Page<GiftCertificate> expected = new PageImpl<>(list, pageable, totalEntities);

        Page<GiftCertificate> actual = giftCertificateDao.findAll(pageable);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findAll")
    void shouldReturnEmptyListIfNoSuchPage() {
        long totalEntities = countTotalEntities();
        List<GiftCertificate> list = Collections.emptyList();
        PageRequest pageable = PageRequest.of(15, 20);
        Page<GiftCertificate> expected = new PageImpl<>(list, pageable, totalEntities);

        Page<GiftCertificate> actual = giftCertificateDao.findAll(pageable);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findById")
    void shouldFindByIdExistingCertificate() {
        Long id = 3L;
        Optional<GiftCertificate> expected = Optional.of(entityManager.find(GiftCertificate.class, id));

        Optional<GiftCertificate> actual = giftCertificateDao.findById(id);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findById")
    void shouldReturnEmptyOptionalIfNoSuchId() {
        Long id = 300L;

        Optional<GiftCertificate> actual = giftCertificateDao.findById(id);

        assertThat(actual).isEmpty();
    }

    @Test
    @Tag("create")
    void shouldSetIdForNewCertificate() {
        GiftCertificate newCertificate = GiftCertificate.builder()
                .name("New certificate")
                .description("For test only")
                .price(500L)
                .duration(6)
                .build();

        giftCertificateDao.create(newCertificate);

        assertThat(newCertificate.getId()).isNotNull();
    }

    @Test
    @Tag("create")
    void shouldSetAuditableFieldsBeforeCreation() {
        GiftCertificate newCertificate = GiftCertificate.builder()
                .name("New certificate")
                .description("For test only")
                .price(500L)
                .duration(6)
                .build();

        giftCertificateDao.create(newCertificate);

        assertThat(newCertificate.getCreateDate()).isNotNull();
        assertThat(newCertificate.getLastUpdateDate()).isNotNull();
    }

    @Test
    @Tag("create")
    void shouldCreateNewCertificateInDatabase() {
        GiftCertificate newCertificate = GiftCertificate.builder()
                .name("New certificate")
                .description("For test only")
                .price(500L)
                .duration(6)
                .build();

        giftCertificateDao.create(newCertificate);
        GiftCertificate actual = entityManager.find(GiftCertificate.class, newCertificate.getId());

        assertThat(actual).isEqualTo(newCertificate);
    }

    @Test
    @Tag("update")
    void shouldUpdateAllFieldsOfCertificateInDatabase() {
        Long id = 1L;
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        giftCertificate.setName("Updated certificate");
        giftCertificate.setDescription("Up to date now");
        giftCertificate.setPrice(200L);
        giftCertificate.setDuration(1);
        giftCertificate.setTags(Collections.emptyList());

        giftCertificateDao.update(giftCertificate);
        GiftCertificate actual = entityManager.find(GiftCertificate.class, id);

        assertThat(actual).isEqualTo(giftCertificate);
    }

    @Test
    @Tag("update")
    void shouldUpdateLastUpdateDateField() {
        Long id = 1L;
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        giftCertificate.setName("Updated certificate");
        giftCertificate.setDescription("Up to date now");
        giftCertificate.setPrice(200L);
        giftCertificate.setDuration(1);
        giftCertificate.setTags(Collections.emptyList());

        LocalDateTime oldLastUpdateDate = giftCertificate.getLastUpdateDate();

        giftCertificateDao.update(giftCertificate);
        entityManager.flush();
        GiftCertificate updatedCertificate = entityManager.find(GiftCertificate.class, id);
        LocalDateTime actual = updatedCertificate.getLastUpdateDate();

        assertThat(actual).isAfter(oldLastUpdateDate);
    }

    @Test
    @Tag("update")
    void shouldNotUpdateCreateDateField() {
        Long id = 1L;
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        giftCertificate.setName("Updated certificate");
        giftCertificate.setDescription("Up to date now");
        giftCertificate.setPrice(200L);
        giftCertificate.setDuration(1);
        giftCertificate.setTags(Collections.emptyList());

        LocalDateTime expected = giftCertificate.getCreateDate();

        giftCertificateDao.update(giftCertificate);
        GiftCertificate updatedCertificate = entityManager.find(GiftCertificate.class, id);
        LocalDateTime actual = updatedCertificate.getCreateDate();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("update")
    void shouldUpdateCertainFields() {
        Long id = 1L;
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(id)
                .description("Up to date now")
                .build();

        giftCertificateDao.update(giftCertificate);

        GiftCertificate actual = entityManager.find(GiftCertificate.class, id);

        assertThat(actual.getDescription()).isEqualTo(giftCertificate.getDescription());
    }

    @Test
    @Tag("delete")
    void shouldDeleteExistingCertificate() {
        Long id = 1L;

        boolean isDeleted = giftCertificateDao.delete(id);
        GiftCertificate actual = entityManager.find(GiftCertificate.class, id);

        assertThat(isDeleted).isTrue();
        assertThat(actual).isNull();
    }

    @Test
    @Tag("delete")
    void shouldReturnFalseIfNoSuchId() {
        Long id = 100L;

        boolean actual = giftCertificateDao.delete(id);

        assertThat(actual).isFalse();
    }

    @Test
    @Tag("count")
    void shouldCountTotalEntities() {
        long expected = countTotalEntities();

        long actual = giftCertificateDao.count();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findAllWithFilters")
    void shouldFindAllEntitiesIfNoFiltersAreGiven() {
        List<GiftCertificate> list = entityManager.createQuery("FROM GiftCertificate", GiftCertificate.class)
                .getResultList();
        GiftCertificateFilters emptyFilters = new GiftCertificateFilters(
                Collections.emptyList(), null, null, Collections.emptyList());
        Page<GiftCertificate> expected = new PageImpl<>(list, DEFAULT_PAGEABLE, list.size());

        Page<GiftCertificate> actual = giftCertificateDao.findAll(emptyFilters, DEFAULT_PAGEABLE);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findAllWithFilters")
    void shouldFindByOneTag() {
        String tagName = "extreme";
        List<GiftCertificate> list = entityManager.createQuery(
                        "SELECT c FROM GiftCertificate c " +
                                "join c.tags t where t.name = :name", GiftCertificate.class)
                .setParameter("name", tagName)
                .getResultList();
        GiftCertificateFilters emptyFilters = new GiftCertificateFilters(
                List.of(tagName), null, null, Collections.emptyList());
        Page<GiftCertificate> expected = new PageImpl<>(list, DEFAULT_PAGEABLE, list.size());

        Page<GiftCertificate> actual = giftCertificateDao.findAll(emptyFilters, DEFAULT_PAGEABLE);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findAllWithFilters")
    void shouldFindBySeveralTagsAndNameContains() {
        Long id = 3L;
        List<GiftCertificate> list = List.of(entityManager.find(GiftCertificate.class, id));
        GiftCertificateFilters emptyFilters = new GiftCertificateFilters(
                List.of("ski", "winter"), "rtif", null, Collections.emptyList());
        Page<GiftCertificate> expected = new PageImpl<>(list, DEFAULT_PAGEABLE, list.size());

        Page<GiftCertificate> actual = giftCertificateDao.findAll(emptyFilters, DEFAULT_PAGEABLE);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findAllWithFilters")
    void shouldFindNothingIfNothingMatchesGivenFilters() {
        GiftCertificateFilters emptyFilters = new GiftCertificateFilters(
                List.of("ski", "winter"), "rtif", "arc", Collections.emptyList());
        Page<GiftCertificate> expected = new PageImpl<>(Collections.emptyList(), DEFAULT_PAGEABLE, 0);

        Page<GiftCertificate> actual = giftCertificateDao.findAll(emptyFilters, DEFAULT_PAGEABLE);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findAllWithFilters")
    void shouldFindByFiltersAndOrderByNameDesc() {
        List<GiftCertificate> list = entityManager.createQuery(
                        "FROM GiftCertificate WHERE id IN (3, 4) " +
                                "ORDER BY name DESC", GiftCertificate.class)
                .getResultList();
        GiftCertificateFilters emptyFilters = new GiftCertificateFilters(
                List.of("ski", "winter"), null, null, List.of("-name"));
        Page<GiftCertificate> expected = new PageImpl<>(list, DEFAULT_PAGEABLE, list.size());

        Page<GiftCertificate> actual = giftCertificateDao.findAll(emptyFilters, DEFAULT_PAGEABLE);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findAllWithFilters")
    void shouldFindByFiltersAndOrderByNameAsc() {
        List<GiftCertificate> list = entityManager.createQuery(
                        "FROM GiftCertificate WHERE id IN (3, 4) " +
                                "ORDER BY name", GiftCertificate.class)
                .getResultList();
        GiftCertificateFilters emptyFilters = new GiftCertificateFilters(
                List.of("ski", "winter"), null, null, List.of("description"));
        Page<GiftCertificate> expected = new PageImpl<>(list, DEFAULT_PAGEABLE, list.size());

        Page<GiftCertificate> actual = giftCertificateDao.findAll(emptyFilters, DEFAULT_PAGEABLE);

        assertThat(actual).isEqualTo(expected);
    }
}
