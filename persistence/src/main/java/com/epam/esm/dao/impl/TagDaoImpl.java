package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TagDaoImpl extends AbstractDao<Tag> implements TagDao {

    private static final String TABLE_NAME = "tag";
    private static final String ID_COLUMN_NAME = "id";
    private static final String NAME_COLUMN_NAME = "name";

    private static final String FIND_ALL_SQL = "SELECT id, name FROM tag";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE id = ?";
    private static final String FIND_BY_NAME_SQL = FIND_ALL_SQL + " WHERE name = ?";

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Tag.class);
    }

    @Override
    public List<Tag> findAll() {
        return executeSelectQuery(FIND_ALL_SQL);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return executeIdentifiableSelectQuery(FIND_BY_ID_SQL, id);
    }

    @Override
    public void create(Tag entity) {
        Long generatedId = executeInsertQueryReturnGeneratedKey(entity, Long.class);
        entity.setId(generatedId);
    }

    @Override
    public void update(Tag entity) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void delete(Long id) {
        executeDeleteQuery(id);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return executeIdentifiableSelectQuery(FIND_BY_NAME_SQL, name);
    }

    @Override
    protected Map<String, Object> getMapOfAllColumnNamesVsArgs(Tag entity) {
        var id = entity.getId();
        var name = entity.getName();

        HashMap<String, Object> map = new HashMap<>();
        map.put(ID_COLUMN_NAME, id);
        map.put(NAME_COLUMN_NAME, name);

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
