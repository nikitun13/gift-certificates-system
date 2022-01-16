package com.epam.esm.dao.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.Entry;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public abstract class AbstractDao<E> {

    protected static final String FIELD_EQUALS_SQL_FORMAT = "%s = ?";
    protected static final String DELETE_SQL_FORMAT = "DELETE FROM %s WHERE %s = ?";

    private static final String UPDATE_ARGS_DELIMITER = ", ";
    private static final String UPDATE_TABLE_SQL_FORMAT = "UPDATE %s SET ";
    private static final String WHERE_ID_SQL_FORMAT = " WHERE %s = ?";

    protected final JdbcTemplate jdbcTemplate;

    protected AbstractDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected List<E> executeSelectQuery(String sql, Object... args) {
        RowMapper<E> rowMapper = getRowMapper();
        return jdbcTemplate.query(sql, rowMapper, args);
    }

    protected Optional<E> executeIdentifiableSelectQuery(String sql, Object... args) {
        return executeSelectQuery(sql, args).stream().findFirst();
    }

    protected <T> T executeInsertQueryReturnGeneratedKeys(E entity, Class<T> keyClass) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = buildInsertPreparedStatement(entity);
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKeyAs(keyClass);
    }

    protected void executeDeleteQuery(Object id) {
        String sql = DELETE_SQL_FORMAT.formatted(getTableName(), getIdColumnName());
        jdbcTemplate.update(sql, id);
    }

    protected int executeUpdateQuery(E entity) {
        Map<String, Object> allColumnNamesVsArgs = getMapOfAllColumnNamesVsArgs(entity);
        Object id = allColumnNamesVsArgs.get(getIdColumnName());
        Map<String, Object> updatableColumnNamesVsArgs = allColumnNamesVsArgs.entrySet().stream()
                .filter(not(entry -> entry.getKey().equals(getIdColumnName())))
                .filter(entry -> ObjectUtils.allNotNull(entry.getValue()))
                .collect(toMap(Entry::getKey, Entry::getValue));

        String prefix = UPDATE_TABLE_SQL_FORMAT.formatted(getTableName());
        String suffix = WHERE_ID_SQL_FORMAT.formatted(getIdColumnName());

        String sql = updatableColumnNamesVsArgs.keySet()
                .stream()
                .map(FIELD_EQUALS_SQL_FORMAT::formatted)
                .collect(joining(UPDATE_ARGS_DELIMITER, prefix, suffix));

        ArrayList<Object> objects = new ArrayList<>(updatableColumnNamesVsArgs.values());
        objects.add(id);
        Object[] args = objects.toArray();

        return jdbcTemplate.update(sql, args);
    }

    protected void setArgumentsToPreparedStatement(PreparedStatement preparedStatement,
                                                   Object... args) throws SQLException {
        int i = 0;
        for (Object arg : args) {
            preparedStatement.setObject(++i, arg);
        }
    }

    protected abstract RowMapper<E> getRowMapper();

    protected abstract PreparedStatementCreator buildInsertPreparedStatement(E entity);

    protected abstract Map<String, Object> getMapOfAllColumnNamesVsArgs(E entity);

    protected abstract String getTableName();

    protected abstract String getIdColumnName();
}
