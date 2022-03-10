package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class TagDaoImpl extends AbstractDao<Long, Tag> implements TagDao {

    /**
     * Native sql query that finds the most widely used tag of a user
     * with the highest cost of all orders.
     */
    private static final String TOP_TAG_SQL = """
            SELECT t.id, t.name
            FROM (SELECT id
                  FROM orders
                  WHERE user_id = (SELECT user_id
                                   FROM orders
                                   GROUP BY user_id
                                   ORDER BY sum(total_price) DESC
                                   LIMIT 1) -- select top user
                 ) o -- select all orders by this user
                     JOIN order_detail od on o.id = od.order_id
                     JOIN gift_certificate gc on gc.id = od.gift_certificate_id
                     JOIN gift_certificate_tag gct on gc.id = gct.gift_certificate_id
                     JOIN tag t on t.id = gct.tag_id
            GROUP BY t.id
            ORDER BY sum(od.quantity) DESC -- most widely used tag of the given orders
            LIMIT 1""";

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
    @SuppressWarnings("unchecked")
    public Optional<Tag> findTopTagOfUserWithTheHighestCostOfAllOrders() {
        return entityManager.createNativeQuery(TOP_TAG_SQL, Tag.class)
                .getResultStream()
                .findFirst();
    }

    @Override
    protected void setNotNullFieldsToManagedEntity(Tag managedEntity, Tag newEntity) {
        var name = Optional.ofNullable(newEntity.getName());
        var certificates = newEntity.getGiftCertificates();
        name.ifPresent(managedEntity::setName);
        managedEntity.getGiftCertificates().addAll(certificates);
    }
}
