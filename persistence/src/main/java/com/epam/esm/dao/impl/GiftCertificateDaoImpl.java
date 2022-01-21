package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl extends AbstractDao<GiftCertificate> implements GiftCertificateDao {

    private static final String TABLE_NAME = "gift_certificate";
    private static final String ID_COLUMN_NAME = "id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String DESCRIPTION_COLUMN_NAME = "description";
    private static final String PRICE_COLUMN_NAME = "price";
    private static final String DURATION_COLUMN_NAME = "duration";
    private static final String CREATE_DATE_COLUMN_NAME = "create_date";
    private static final String LAST_UPDATE_DATE_COLUMN_NAME = "last_update_date";

    private static final String FIND_ALL_SQL = """
            SELECT id, name, description, price, duration, create_date, last_update_date
            FROM gift_certificate""";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE id = ?";

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, GiftCertificate.class);
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
    public void update(GiftCertificate entity) {
        executeUpdateQuery(entity);
    }

    @Override
    public void delete(Long id) {
        executeDeleteQuery(id);
    }

    @Override
    protected Map<String, Object> getMapOfAllColumnNamesVsArgs(GiftCertificate entity) {
        var id = entity.getId();
        var name = entity.getName();
        var description = entity.getDescription();
        var price = entity.getPrice();
        var duration = entity.getDuration();
        var createDate = entity.getCreateDate();
        var lastUpdateDate = entity.getLastUpdateDate();

        HashMap<String, Object> map = new HashMap<>();
        map.put(ID_COLUMN_NAME, id);
        map.put(NAME_COLUMN_NAME, name);
        map.put(DESCRIPTION_COLUMN_NAME, description);
        map.put(PRICE_COLUMN_NAME, price);
        map.put(DURATION_COLUMN_NAME, duration);
        map.put(CREATE_DATE_COLUMN_NAME, createDate);
        map.put(LAST_UPDATE_DATE_COLUMN_NAME, lastUpdateDate);

        return map;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getIdColumnName() {
        return ID_COLUMN_NAME;
    }
}
