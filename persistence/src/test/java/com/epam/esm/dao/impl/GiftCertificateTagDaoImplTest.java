package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoTestConfig;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = DaoTestConfig.class)
@ExtendWith(SpringExtension.class)
@Transactional
class GiftCertificateTagDaoImplTest {

    private final GiftCertificateTagDao dao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    GiftCertificateTagDaoImplTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.dao = new GiftCertificateTagDaoImpl(jdbcTemplate);
    }

    @Test
    @Tag("create")
    void shouldCreateManyToManyRelationship() {
        Long giftCertificateId = 4L;
        Long tagId = 4L;

        dao.create(giftCertificateId, tagId);
        String sql = """
                SELECT *
                FROM gift_certificate_tag
                JOIN gift_certificate gc on gc.id = gift_certificate_tag.gift_certificate_id
                JOIN tag on tag.id = gift_certificate_tag.tag_id
                WHERE tag.id = ? AND gift_certificate_id = ?""";
        List<GiftCertificate> actual = jdbcTemplate.query(sql, new DataClassRowMapper<>(GiftCertificate.class),
                giftCertificateId, tagId);

        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    @Tag("create")
    void shouldThrowDuplicateKeyExceptionIfRelationshipAlreadyExists() {
        Long giftCertificateId = 2L;
        Long tagId = 3L;

        assertThatThrownBy(() -> dao.create(giftCertificateId, tagId))
                .isExactlyInstanceOf(DuplicateKeyException.class);
    }
}
