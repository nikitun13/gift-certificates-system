package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class UserDaoImpl extends AbstractDao<Long, User> implements UserDao {

    public UserDaoImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    protected void mergeEntities(User persistedEntity, User newEntity) {
        var username = Optional.ofNullable(newEntity.getUsername());
        newEntity.getOrders()
                .forEach(persistedEntity::addOrder);

        username.ifPresent(persistedEntity::setUsername);
    }
}
