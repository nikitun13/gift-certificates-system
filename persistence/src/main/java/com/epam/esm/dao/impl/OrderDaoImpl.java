package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dto.Page;
import com.epam.esm.entity.Order;
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
    protected void mergeEntities(Order persistedEntity, Order newEntity) {
        var user = Optional.ofNullable(newEntity.getUser());
        var createDate = Optional.ofNullable(newEntity.getCreateDate());
        var totalPrice = Optional.ofNullable(newEntity.getTotalPrice());

        newEntity.getDetails().forEach(persistedEntity::addDetail);
        user.ifPresent(persistedEntity::setUser);
        createDate.ifPresent(persistedEntity::setCreateDate);
        totalPrice.ifPresent(persistedEntity::setTotalPrice);
    }

    @Override
    public List<Order> findByUserId(Long userId, Page page) {
        int offset = page.getOffset();
        int pageSize = page.pageSize();
        return entityManager.createQuery("FROM Order WHERE user_id = :userId", Order.class)
                .setParameter("userId", userId)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<Order> findByUserIdAndId(Long userId, Long id) {
        return entityManager.createQuery("FROM Order WHERE id = :id AND user_id = :userId", Order.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst();
    }
}
