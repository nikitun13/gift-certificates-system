package com.epam.esm.dao.impl;

import com.epam.esm.util.ReflectUtil;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.Entry;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public abstract class AbstractDao<E> {

    protected static final String WHERE_SQL = " WHERE ";
    protected static final String ORDER_BY_SQL = " ORDER BY ";
    protected static final String AND_SQL = " AND ";
    protected static final String COMMA_DELIMITER = ", ";
    protected static final String FIELD_EQUALS_SQL_FORMAT = "%s = ?";
    protected static final String UPDATE_TABLE_SQL_FORMAT = "UPDATE %s SET ";
    protected static final String WHERE_FIELD_EQUALS_SQL_FORMAT = WHERE_SQL + FIELD_EQUALS_SQL_FORMAT;
    protected static final String DELETE_SQL_FORMAT = "DELETE FROM %s" + WHERE_FIELD_EQUALS_SQL_FORMAT;

    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<E> rowMapper;

    protected AbstractDao(JdbcTemplate jdbcTemplate, Class<E> entityClass) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new DataClassRowMapper<>(entityClass);
    }

    protected List<E> executeSelectQuery(String sql, Object... args) {
        return jdbcTemplate.query(sql, rowMapper, args);
    }

    protected Optional<E> executeIdentifiableSelectQuery(String sql, Object... args) {
        return executeSelectQuery(sql, args).stream().findFirst();
    }

    protected <T> T executeInsertQueryReturnGeneratedKey(E entity, Class<T> keyClass) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(getTableName())
                .usingGeneratedKeyColumns(getIdColumnName())
                .executeAndReturnKeyHolder(new BeanPropertySqlParameterSource(entity))
                .getKeyAs(keyClass);
    }

    protected void executeDeleteQuery(Object id) {
        String sql = DELETE_SQL_FORMAT.formatted(getTableName(), getIdColumnName());
        jdbcTemplate.update(sql, id);
    }

    protected int executeUpdateQuery(E entity) {
        Map<String, Optional<Object>> allColumnNamesWithArgs = ReflectUtil.getAllFieldNamesWithValues(entity).entrySet().stream()
                .collect(toMap(entry -> camelToSnakeCase(entry.getKey()), Entry::getValue));
        String idColumnName = getIdColumnName();
        Object id = allColumnNamesWithArgs.remove(idColumnName).orElseThrow(IllegalArgumentException::new);
        Map<String, Object> updatableColumnNamesWithArgs = allColumnNamesWithArgs.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent())
                .collect(toMap(Entry::getKey, entry -> entry.getValue().get()));
        String prefix = UPDATE_TABLE_SQL_FORMAT.formatted(getTableName());
        String suffix = WHERE_FIELD_EQUALS_SQL_FORMAT.formatted(idColumnName);

        String sql = updatableColumnNamesWithArgs.keySet()
                .stream()
                .map(FIELD_EQUALS_SQL_FORMAT::formatted)
                .collect(joining(COMMA_DELIMITER, prefix, suffix));

        ArrayList<Object> objects = new ArrayList<>(updatableColumnNamesWithArgs.values());
        objects.add(id);
        Object[] args = objects.toArray();

        return jdbcTemplate.update(sql, args);
    }

    protected String camelToSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }

    protected String createOrderingSqlString(String field) {
        return field.startsWith("-")
                ? field.substring(1) + " DESC"
                : field;
    }

    protected String wrapWithPercentages(String str) {
        return "%" + str + "%";
    }

    protected abstract String getTableName();

    protected abstract String getIdColumnName();
}
