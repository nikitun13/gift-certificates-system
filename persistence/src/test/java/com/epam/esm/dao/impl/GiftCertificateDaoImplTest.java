package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoTestConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = DaoTestConfig.class)
@ExtendWith(SpringExtension.class)
@Transactional
class GiftCertificateDaoImplTest {

    private GiftCertificate cocaColaCertificate;
    private GiftCertificate kfcCertificate;
    private GiftCertificate oneHundredDollarsCertificate;
    private GiftCertificate NoTagsCertificate;

    @BeforeEach
    void init() {
        cocaColaCertificate = GiftCertificate.builder()
                .id(1L)
                .name("Free coca-cola certificate")
                .description("Simple use it and get coca-cola")
                .price(1000L)
                .duration(10)
                .createDate(LocalDateTime.parse("2022-01-01T12:00:00"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-02T13:30:25"))
                .build();

        kfcCertificate = GiftCertificate.builder()
                .id(2L)
                .name("KFC certificate")
                .description("10 dollars certificate")
                .price(1000L)
                .duration(10)
                .createDate(LocalDateTime.parse("2022-01-03T16:30:20"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-03T16:30:20"))
                .build();

        oneHundredDollarsCertificate = GiftCertificate.builder()
                .id(3L)
                .name("100 dollars certificate")
                .description("Buy anything for 100 dollars")
                .price(10000L)
                .duration(20)
                .createDate(LocalDateTime.parse("2022-01-05T14:29:12"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-05T14:29:12"))
                .build();

        NoTagsCertificate = GiftCertificate.builder()
                .id(4L)
                .name("Awesome certificate without tags")
                .description("No tags provided")
                .price(100L)
                .duration(3)
                .createDate(LocalDateTime.parse("2022-01-10T17:19:46"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-11T12:19:46"))
                .build();
    }

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @Test
    @Tag("findAll")
    void shouldFindAllCertificates() {
        List<GiftCertificate> expected = List.of(
                cocaColaCertificate,
                kfcCertificate,
                oneHundredDollarsCertificate,
                NoTagsCertificate
        );

        List<GiftCertificate> actual = giftCertificateDao.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @Tag("findById")
    void shouldFindByIdExistingCertificate() {
        Optional<GiftCertificate> expected = Optional.of(oneHundredDollarsCertificate);

        Optional<GiftCertificate> actual = giftCertificateDao.findById(3L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findById")
    void shouldReturnEmptyOptionalForFindByIdIfNoExistingCertificate() {
        Optional<GiftCertificate> actual = giftCertificateDao.findById(500L);

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
                .createDate(LocalDateTime.parse("2022-01-16T10:19:46"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-16T10:19:46"))
                .build();

        giftCertificateDao.create(newCertificate);

        assertThat(newCertificate.getId()).isNotNull();
    }

    @Test
    @Tag("create")
    void shouldCreateNewCertificateInDatabase() {
        GiftCertificate newCertificate = GiftCertificate.builder()
                .name("New certificate")
                .description("For test only")
                .price(500L)
                .duration(6)
                .createDate(LocalDateTime.parse("2022-01-16T10:19:46"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-16T10:19:46"))
                .build();

        giftCertificateDao.create(newCertificate);
        Optional<GiftCertificate> expected = Optional.of(newCertificate);
        Optional<GiftCertificate> actual = giftCertificateDao.findById(newCertificate.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("update")
    void shouldUpdateAllFieldsOfCertificateInDatabase() {
        kfcCertificate.setName("KFC updated certificate");
        kfcCertificate.setDescription("Up to date now");
        kfcCertificate.setPrice(200L);
        kfcCertificate.setDuration(1);
        kfcCertificate.setCreateDate(LocalDateTime.parse("2022-01-16T15:19:46"));
        kfcCertificate.setLastUpdateDate(LocalDateTime.parse("2022-01-16T18:19:46"));

        giftCertificateDao.update(kfcCertificate);
        Optional<GiftCertificate> expected = Optional.of(kfcCertificate);
        Optional<GiftCertificate> actual = giftCertificateDao.findById(kfcCertificate.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("update")
    void shouldUpdateCertainFieldsOfCertificateInDatabase() {
        String newName = "Very awesome cola certificate!";
        LocalDateTime newLastUpdateDate = LocalDateTime.parse("2022-01-17T10:10:10");
        cocaColaCertificate.setName(newName);
        cocaColaCertificate.setLastUpdateDate(newLastUpdateDate);
        GiftCertificate updatedCertificate = GiftCertificate.builder()
                .id(cocaColaCertificate.getId())
                .name(newName)
                .lastUpdateDate(newLastUpdateDate)
                .build();

        giftCertificateDao.update(updatedCertificate);
        Optional<GiftCertificate> expected = Optional.of(cocaColaCertificate);
        Optional<GiftCertificate> actual = giftCertificateDao.findById(updatedCertificate.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("delete")
    void shouldDeleteExistingCertificate() {
        List<GiftCertificate> expected = List.of(
                cocaColaCertificate,
                kfcCertificate,
                oneHundredDollarsCertificate
        );

        giftCertificateDao.delete(NoTagsCertificate.getId());
        List<GiftCertificate> actual = giftCertificateDao.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @Tag("delete")
    void shouldNotDeleteAnythingIfNotSuchId() {
        List<GiftCertificate> expected = List.of(
                cocaColaCertificate,
                kfcCertificate,
                oneHundredDollarsCertificate,
                NoTagsCertificate
        );

        giftCertificateDao.delete(5000L);
        List<GiftCertificate> actual = giftCertificateDao.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @Tag("findByParams")
    void shouldFindAllIfEmptyParamsAndOrderByAreGiven() {
        List<GiftCertificate> expected = List.of(
                cocaColaCertificate,
                kfcCertificate,
                oneHundredDollarsCertificate,
                NoTagsCertificate
        );

        List<GiftCertificate> actual = giftCertificateDao.findByParams(
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyList());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByParams")
    void shouldFindAllAndOrderByNameDescIfEmptyParamsAndOrderByNameDescAreGiven() {
        List<GiftCertificate> expected = List.of(
                kfcCertificate,
                cocaColaCertificate,
                NoTagsCertificate,
                oneHundredDollarsCertificate
        );

        List<GiftCertificate> actual = giftCertificateDao.findByParams(
                Collections.emptyMap(),
                Collections.emptyMap(),
                List.of("-name"));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByParams")
    void shouldFindAllAndOrderByGivenArgsIfEmptyParamsAreGiven() {
        List<GiftCertificate> expected = List.of(
                kfcCertificate,
                cocaColaCertificate,
                NoTagsCertificate,
                oneHundredDollarsCertificate
        );

        List<GiftCertificate> actual = giftCertificateDao.findByParams(
                Collections.emptyMap(),
                Collections.emptyMap(),
                List.of("-name", "createDate", "-lastUpdateDate"));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByParams")
    void shouldThrowBadSqlGrammarExceptionIfInvalidOrderingFieldReceived() {
        List<String> orderBy = List.of("-name", "createDate", "-time");
        Map<String, String> emptyParams = Collections.emptyMap();
        assertThatThrownBy(() -> giftCertificateDao.findByParams(
                emptyParams, emptyParams, orderBy))
                .isExactlyInstanceOf(BadSqlGrammarException.class);
    }

    @Test
    @Tag("findByParams")
    void shouldFilterByOneFieldWithoutOrdering() {
        List<GiftCertificate> expected = List.of(
                cocaColaCertificate,
                oneHundredDollarsCertificate
        );
        Map<String, String> certificateParams = Map.of("name~", "la");

        List<GiftCertificate> actual = giftCertificateDao.findByParams(
                certificateParams, Collections.emptyMap(), Collections.emptyList()
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByParams")
    void shouldFindByParamsAndTagAndOrderByDateDescThanName() {
        List<GiftCertificate> expected = List.of(
                oneHundredDollarsCertificate,
                kfcCertificate
        );
        Map<String, String> certificateParams = Map.of(
                "name~", "ti",
                "description~", "10"
        );
        Map<String, String> tagParams = Map.of("name", "money certificate");
        List<String> orderBy = List.of("-createDate", "name");

        List<GiftCertificate> actual = giftCertificateDao.findByParams(
                certificateParams, tagParams, orderBy
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findByParams")
    void shouldFindCertificatesOnlyByTag() {
        List<GiftCertificate> expected = List.of(
                oneHundredDollarsCertificate,
                kfcCertificate
        );
        Map<String, String> tagParams = Map.of("name", "money certificate");
        List<String> emptyOrderBy = Collections.emptyList();

        List<GiftCertificate> actual = giftCertificateDao.findByParams(
                Collections.emptyMap(), tagParams, emptyOrderBy
        );

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @Tag("findByParams")
    void shouldFindCertificatesByPriceBetween() {
        List<GiftCertificate> expected = List.of(
                cocaColaCertificate,
                kfcCertificate
        );
        Map<String, String> certificateParams = Map.of(
                "price>", "500",
                "price<", "5000"
        );

        List<GiftCertificate> actual = giftCertificateDao.findByParams(
                certificateParams, Collections.emptyMap(), Collections.emptyList()
        );

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @Tag("findByParams")
    void shouldFindCertificatesByDifferentParamsAndOrderBy() {
        List<GiftCertificate> expected = List.of(oneHundredDollarsCertificate);
        Map<String, String> certificateParams = Map.of(
                "price>", "0",
                "price<", "10000",
                "name~", "dollar",
                "description~", "10",
                "createDate>", "2022-01-05",
                "duration", "20"
        );
        Map<String, String> tagsParams = Map.of(
                "name~", "ce",
                "id>", "2"
        );
        List<String> orderBy = List.of("-createDate", "name", "-duration", "lastUpdateDate");

        List<GiftCertificate> actual = giftCertificateDao.findByParams(
                certificateParams, tagsParams, orderBy
        );

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @Tag("findByParams")
    void shouldFindByCreateDateBetween() {
        List<GiftCertificate> expected = List.of(
                kfcCertificate,
                oneHundredDollarsCertificate,
                NoTagsCertificate
        );
        Map<String, String> certificateParams = Map.of(
                "createDate>", "2022-01-02",
                "createDate<", "2022-01-11"
        );

        List<GiftCertificate> actual = giftCertificateDao.findByParams(
                certificateParams, Collections.emptyMap(), Collections.emptyList()
        );

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
