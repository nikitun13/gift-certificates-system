package com.epam.esm.entity.listener;

import com.epam.esm.entity.AuditableEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditListener {

    @PrePersist
    public void prePersist(AuditableEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateDate(now);
        entity.setLastUpdateDate(now);
    }

    @PreUpdate
    public void preUpdate(AuditableEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setLastUpdateDate(now);
    }
}
