package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl extends AbstractDao<Long, Order> implements OrderDao {

    public OrderDaoImpl(EntityManager entityManager) {
        super(Order.class, entityManager);
    }

    @Override
    protected void setNotNullFieldsToManagedEntity(Order managedEntity, Order newEntity) {
        var user = Optional.ofNullable(newEntity.getUser());
        var createDate = Optional.ofNullable(newEntity.getCreateDate());
        var totalPrice = Optional.ofNullable(newEntity.getTotalPrice());

        newEntity.getDetails().forEach(managedEntity::addDetail);
        user.ifPresent(managedEntity::setUser);
        createDate.ifPresent(managedEntity::setCreateDate);
        totalPrice.ifPresent(managedEntity::setTotalPrice);
    }

    @Override
    public Page<Order> findByUser(User user, Pageable pageable) {
        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();
        List<Order> content = entityManager.createQuery(
                        "SELECT o FROM Order o WHERE o.user = :user", Order.class)
                .setParameter("user", user)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
        long total = countByUser(user);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<Order> findByUserAndId(User user, Long id) {
        return entityManager.createQuery(
                        "SELECT o FROM Order o WHERE o.id = :id AND o.user = :user", Order.class)
                .setParameter("id", id)
                .setParameter("user", user)
                .getResultStream()
                .findFirst();
    }

    @Override
    public long countByUser(User user) {
        return entityManager.createQuery("SELECT count(o) FROM Order o WHERE o.user = :user", Long.class)
                .setParameter("user", user)
                .getSingleResult();
    }
}
