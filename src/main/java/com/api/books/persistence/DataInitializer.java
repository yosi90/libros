package com.api.books.persistence;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.api.books.persistence.entities.RoleEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Component
public class DataInitializer {
    
    @PersistenceContext
    private EntityManager entityManager;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void Init() {
        if(isEmpty("RoleEntity"))
            createRoles();
    }

    private boolean isEmpty(String entityName) {
        String queryStr = "SELECT COUNT(e) FROM " + entityName + " e";
        TypedQuery<Long> query = entityManager.createQuery(queryStr, Long.class);
        return query.getSingleResult() == 0;
    }

    private void createRoles() {
        RoleEntity roleUser = new RoleEntity("USER");
        RoleEntity roleAdmin = new RoleEntity("ADMIN");

        entityManager.persist(roleUser);
        entityManager.persist(roleAdmin);
    }
}
