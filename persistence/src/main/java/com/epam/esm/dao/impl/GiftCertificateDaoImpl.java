package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.SqlRequestParamParser;
import com.epam.esm.entity.GiftCertificate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

@Repository
public class GiftCertificateDaoImpl extends AbstractDao<GiftCertificate> implements GiftCertificateDao {

    private static final Logger log = LoggerFactory.getLogger(GiftCertificateDaoImpl.class);

    private static final String TABLE_NAME = "gift_certificate";
    private static final String ID_COLUMN_NAME = "id";

    private static final String GIFT_CERTIFICATE_PREFIX_FORMAT = "gift_certificate.%s";
    private static final String TAG_PREFIX_FORMAT = "tag.%s";

    private static final String FIND_ALL_SQL = """
            SELECT id, name, description, price, duration, create_date, last_update_date
            FROM gift_certificate""";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE id = ?";
    private static final String SELECT_WITH_PARAMS_SQL = """
            SELECT gift_certificate.id,
                   gift_certificate.name,
                   gift_certificate.description,
                   gift_certificate.price,
                   gift_certificate.duration,
                   gift_certificate.create_date,
                   gift_certificate.last_update_date
            FROM gift_certificate""";
    private static final String JOIN_TAG_SQL = """
             JOIN gift_certificate_tag gct ON gift_certificate.id = gct.gift_certificate_id
            JOIN tag ON tag.id = gct.tag_id""";

    private final SqlRequestParamParser parser;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, SqlRequestParamParser parser) {
        super(jdbcTemplate, GiftCertificate.class);
        this.parser = parser;
    }

    @Override
    public List<GiftCertificate> findAll() {
        return executeSelectQuery(FIND_ALL_SQL);
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return executeIdentifiableSelectQuery(FIND_BY_ID_SQL, id);
    }

    @Override
    public void create(GiftCertificate entity) {
        Long generatedId = executeInsertQueryReturnGeneratedKey(entity, Long.class);
        entity.setId(generatedId);
    }

    @Override
    public boolean update(GiftCertificate entity) {
        return executeUpdateQuery(entity);
    }

    @Override
    public boolean delete(Long id) {
        return executeDeleteQuery(id);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getIdColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    public List<GiftCertificate> findByParams(Map<String, String> certificateProperties,
                                              Map<String, String> tagProperties,
                                              List<String> orderBy) {
        StringBuilder builder = new StringBuilder(SELECT_WITH_PARAMS_SQL);
        Object[] args = null;
        if (ObjectUtils.isNotEmpty(certificateProperties) || ObjectUtils.isNotEmpty(tagProperties)) {
            if (ObjectUtils.isNotEmpty(tagProperties)) {
                builder.append(JOIN_TAG_SQL);
            }
            Stream<Pair<String, String>> certificatePropertiesStream = certificateProperties.entrySet().stream()
                    .map(entry -> Pair.of(
                            GIFT_CERTIFICATE_PREFIX_FORMAT.formatted(entry.getKey()),
                            entry.getValue()
                    ));
            Stream<Pair<String, String>> tagPropertiesStream = tagProperties.entrySet().stream()
                    .map(entry -> Pair.of(
                            TAG_PREFIX_FORMAT.formatted(entry.getKey()),
                            entry.getValue()
                    ));

            Map<String, String> filteringParams = Stream.concat(certificatePropertiesStream, tagPropertiesStream)
                    .collect(toMap(Pair::getLeft,
                            pair -> pair.getLeft().endsWith("~")
                                    ? wrapWithPercentages(pair.getRight())
                                    : pair.getRight()));

            String whereSql = filteringParams.keySet().stream()
                    .map(this::camelToSnakeCase)
                    .map(parser::toFormattedSqlString)
                    .collect(joining(AND_SQL, WHERE_SQL, ""));
            args = filteringParams.values().toArray();
            builder.append(whereSql);
        }

        if (ObjectUtils.isNotEmpty(orderBy)) {
            String orderBySql = orderBy.stream()
                    .map(this::camelToSnakeCase)
                    .map(this::createOrderingSqlString)
                    .collect(joining(COMMA_DELIMITER, ORDER_BY_SQL, ""));
            builder.append(orderBySql);
        }

        String sql = builder.toString();
        log.debug("Built sql: {}", sql);
        String argsToString = ArrayUtils.toString(args);
        log.debug("Filtering args: {}", argsToString);
        return jdbcTemplate.query(sql, rowMapper, args);
    }
}
