package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateFilters;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.JpaUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl extends AbstractDao<Long, GiftCertificate> implements GiftCertificateDao {

    private static final String NAME_ATTRIBUTE = "name";
    private static final String DESCRIPTION_ATTRIBUTE = "description";
    private static final String TAGS_ATTRIBUTE = "tags";

    private final TagDao tagDao;
    private final JpaUtil jpaUtil;

    public GiftCertificateDaoImpl(EntityManager entityManager, TagDao tagDao, JpaUtil jpaUtil) {
        super(GiftCertificate.class, entityManager);
        this.tagDao = tagDao;
        this.jpaUtil = jpaUtil;
    }

    @Override
    protected void setNotNullFieldsToManagedEntity(GiftCertificate managedEntity, GiftCertificate newEntity) {
        newEntity.getTags().stream()
                .map(tagDao::createIfNotExists)
                .forEach(managedEntity::addTag);
        var name = Optional.ofNullable(newEntity.getName());
        var description = Optional.ofNullable(newEntity.getDescription());
        var price = Optional.ofNullable(newEntity.getPrice());
        var duration = Optional.ofNullable(newEntity.getDuration());
        var createDate = Optional.ofNullable(newEntity.getCreateDate());
        var lastUpdateDate = Optional.ofNullable(newEntity.getLastUpdateDate());

        name.ifPresent(managedEntity::setName);
        description.ifPresent(managedEntity::setDescription);
        price.ifPresent(managedEntity::setPrice);
        duration.ifPresent(managedEntity::setDuration);
        createDate.ifPresent(managedEntity::setCreateDate);
        lastUpdateDate.ifPresent(managedEntity::setLastUpdateDate);
    }

    @Override
    public GiftCertificate create(GiftCertificate entity) {
        List<Tag> tags = entity.getTags().stream()
                .map(tagDao::createIfNotExists)
                .toList();
        entity.setTags(tags);
        return super.create(entity);
    }

    @Override
    public Page<GiftCertificate> findAll(GiftCertificateFilters filters, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteria = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificate = criteria.from(GiftCertificate.class);
        Optional<Predicate[]> maybePredicates = buildPredicates(cb, giftCertificate, filters, criteria);
        Optional<Order[]> maybeOrders = buildOrders(cb, giftCertificate, filters.orderBy());

        criteria.select(giftCertificate);
        maybePredicates.ifPresent(criteria::where);
        maybeOrders.ifPresent(criteria::orderBy);

        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();
        List<GiftCertificate> content = entityManager.createQuery(criteria)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
        long total = countTotalByFilters(filters, cb);

        return new PageImpl<>(content, pageable, total);
    }

    private long countTotalByFilters(GiftCertificateFilters filters, CriteriaBuilder cb) {
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<GiftCertificate> giftCertificate = criteria.from(GiftCertificate.class);
        Subquery<GiftCertificate> subquery = criteria.subquery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateSubquery = subquery.from(GiftCertificate.class);
        Optional<Predicate[]> maybePredicates = buildPredicates(
                cb, giftCertificateSubquery, filters, subquery);

        maybePredicates.ifPresent(subquery::where);

        criteria.select(cb.count(giftCertificate))
                .where(giftCertificate.in(
                        subquery.select(giftCertificateSubquery)
                ));
        return entityManager.createQuery(criteria)
                .getSingleResult();
    }

    private Optional<Predicate[]> buildPredicates(CriteriaBuilder cb,
                                                  Root<GiftCertificate> giftCertificate,
                                                  GiftCertificateFilters filters,
                                                  AbstractQuery<GiftCertificate> criteria) {
        String name = filters.name();
        String description = filters.description();
        List<String> tags = filters.tags();

        List<Predicate> predicates = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(name)) {
            String wrappedString = jpaUtil.wrapWithPercentages(name);
            Predicate predicate = cb.like(giftCertificate.get(NAME_ATTRIBUTE), wrappedString);
            predicates.add(predicate);
        }
        if (ObjectUtils.isNotEmpty(description)) {
            String wrappedString = jpaUtil.wrapWithPercentages(description);
            Predicate predicate = cb.like(giftCertificate.get(DESCRIPTION_ATTRIBUTE), wrappedString);
            predicates.add(predicate);
        }
        if (ObjectUtils.isNotEmpty(tags)) {
            Join<GiftCertificate, Tag> tag = giftCertificate.join(TAGS_ATTRIBUTE);
            Path<String> tagNameAttr = tag.get(NAME_ATTRIBUTE);
            tags = tags.stream()
                    .filter(ObjectUtils::isNotEmpty)
                    .distinct()
                    .toList();
            tags.stream()
                    .map(it -> cb.equal(tagNameAttr, it))
                    .reduce(cb::or)
                    .ifPresent(predicates::add);
            criteria.groupBy(giftCertificate)
                    .having(cb.equal(cb.count(tag), tags.size()));
        }
        return predicates.isEmpty()
                ? Optional.empty()
                : Optional.of(predicates.toArray(Predicate[]::new));
    }

    private Optional<Order[]> buildOrders(CriteriaBuilder cb,
                                          Root<GiftCertificate> giftCertificate,
                                          List<String> orderBy) {
        if (ObjectUtils.isNotEmpty(orderBy)) {
            Order[] orders = orderBy.stream()
                    .filter(ObjectUtils::isNotEmpty)
                    .map(field -> jpaUtil.createOrder(cb, giftCertificate, field))
                    .toArray(Order[]::new);
            return Optional.of(orders);
        }
        return Optional.empty();
    }
}
