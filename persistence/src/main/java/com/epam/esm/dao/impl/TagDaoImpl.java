package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class TagDaoImpl extends AbstractDao<Long, Tag> implements TagDao {

    public TagDaoImpl(EntityManager entityManager) {
        super(Tag.class, entityManager);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return entityManager.createQuery(
                        "SELECT t FROM Tag t WHERE t.name = :name", Tag.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Tag createIfNotExists(Tag tag) {
        Optional<Tag> maybeTag = findByName(tag.getName());
        return maybeTag.orElseGet(() -> create(tag));
    }

    @Override
    protected void mergeEntities(Tag persistedEntity, Tag newEntity) {
        var name = Optional.ofNullable(newEntity.getName());
        var certificates = newEntity.getGiftCertificates();
        name.ifPresent(persistedEntity::setName);
        persistedEntity.getGiftCertificates().addAll(certificates);
    }
}
