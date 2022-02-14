package com.epam.esm.dao.impl;

import com.epam.esm.dao.BaseDao;
import com.epam.esm.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<K extends Serializable, E extends BaseEntity<K>> implements BaseDao<K, E> {

    private static final String COUNT_FORMAT = "SELECT COUNT(e) FROM %s e";

    private final Class<E> entityClass;
    protected final EntityManager entityManager;

    protected AbstractDao(Class<E> entityClass, EntityManager entityManager) {
        this.entityClass = entityClass;
        this.entityManager = entityManager;
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        CriteriaQuery<E> criteria = entityManager.getCriteriaBuilder().createQuery(entityClass);
        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();
        criteria.from(entityClass);
        List<E> content = entityManager.createQuery(criteria)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
        long total = count();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    public E create(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public boolean update(E entity) {
        Optional<E> maybeEntity = findById(entity.getId());
        maybeEntity.ifPresent(managedEntity -> update(managedEntity, entity));
        return maybeEntity.isPresent();
    }

    @Override
    public boolean delete(K id) {
        Optional<E> maybeEntity = findById(id);
        maybeEntity.ifPresent(entityManager::remove);
        return maybeEntity.isPresent();
    }

    @Override
    public long count() {
        return entityManager.createQuery(
                        COUNT_FORMAT.formatted(entityClass.getName()), Long.class)
                .getSingleResult();
    }

    /**
     * Merges new entity to managed one.
     * Updates not null fields from the new entity.
     *
     * @param managedEntity managed entity to be updated.
     * @param newEntity     new entity with optional fields for updating.
     */
    protected abstract void setNotNullFieldsToManagedEntity(E managedEntity, E newEntity);

    private void update(E persistedEntity, E newEntity) {
        setNotNullFieldsToManagedEntity(persistedEntity, newEntity);
        entityManager.merge(persistedEntity);
    }
}
