package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateTagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GiftCertificateTagDaoImpl implements GiftCertificateTagDao {

    private static final String INSERT_SQL = """
            INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
            VALUES (?, ?)""";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateTagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Long giftCertificateId, Long tagId) {
        jdbcTemplate.update(INSERT_SQL, giftCertificateId, tagId);
    }
}
