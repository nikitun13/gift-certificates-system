package com.epam.esm.dao.impl;

import com.epam.esm.dao.BaseDao;
import com.epam.esm.entity.BaseEntity;
import com.epam.esm.entity.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractDao<K extends Serializable, E extends BaseEntity<K>> implements BaseDao<K, E> {

    private final Class<E> entityClass;
    protected final EntityManager entityManager;

    protected AbstractDao(Class<E> entityClass, EntityManager entityManager) {
        this.entityClass = entityClass;
        this.entityManager = entityManager;
    }

    @Override
    public List<E> findAll(Page page) {
        CriteriaQuery<E> criteria = entityManager.getCriteriaBuilder().createQuery(entityClass);
        int offset = page.getOffset();
        int pageSize = page.pageSize();
        criteria.from(entityClass);
        return entityManager.createQuery(criteria)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
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
        maybeEntity.ifPresent(persistedEntity -> update(persistedEntity, entity));
        return maybeEntity.isPresent();
    }

    @Override
    public boolean delete(K id) {
        Optional<E> maybeEntity = findById(id);
        maybeEntity.ifPresent(entityManager::remove);
        return maybeEntity.isPresent();
    }

    /**
     * Merges new entity to persisted one.
     * Updates not null fields from the new entity.
     *
     * @param persistedEntity persisted entity to be updated.
     * @param newEntity       new entity with optional fields for updating.
     */
    protected abstract void mergeEntities(E persistedEntity, E newEntity);

    private void update(E persistedEntity, E newEntity) {
        mergeEntities(persistedEntity, newEntity);
        entityManager.merge(persistedEntity);
    }
}
