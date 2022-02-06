package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl extends AbstractDao<Long, GiftCertificate> implements GiftCertificateDao {

    private final TagDao tagDao;

    public GiftCertificateDaoImpl(EntityManager entityManager, TagDao tagDao) {
        super(GiftCertificate.class, entityManager);
        this.tagDao = tagDao;
    }

    @Override
    protected void mergeEntities(GiftCertificate persistedEntity, GiftCertificate newEntity) {
        newEntity.getTags().stream()
                .map(tagDao::createIfNotExists)
                .forEach(persistedEntity::addTag);
        var name = Optional.ofNullable(newEntity.getName());
        var description = Optional.ofNullable(newEntity.getDescription());
        var price = Optional.ofNullable(newEntity.getPrice());
        var duration = Optional.ofNullable(newEntity.getDuration());
        var createDate = Optional.ofNullable(newEntity.getCreateDate());
        var lastUpdateDate = Optional.ofNullable(newEntity.getLastUpdateDate());

        name.ifPresent(persistedEntity::setName);
        description.ifPresent(persistedEntity::setDescription);
        price.ifPresent(persistedEntity::setPrice);
        duration.ifPresent(persistedEntity::setDuration);
        createDate.ifPresent(persistedEntity::setCreateDate);
        lastUpdateDate.ifPresent(persistedEntity::setLastUpdateDate);
    }

    @Override
    public GiftCertificate create(GiftCertificate entity) {
        List<Tag> tags = entity.getTags().stream()
                .map(tagDao::createIfNotExists)
                .toList();
        entity.setTags(tags);
        return super.create(entity);
    }
}
