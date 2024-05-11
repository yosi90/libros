package com.api.books.persistence;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.api.books.persistence.entities.BookStatusEntity;
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
        if(isEmpty("BookStatusEntity"))
            createBookStatuses();
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

    private void createBookStatuses() {
        BookStatusEntity statusToBuy = new BookStatusEntity("Por comprar");
        BookStatusEntity statusOnHold = new BookStatusEntity("En espera");
        BookStatusEntity statusOnGoing = new BookStatusEntity("En marcha");
        BookStatusEntity statusRead = new BookStatusEntity("Leido");

        entityManager.persist(statusToBuy);
        entityManager.persist(statusOnHold);
        entityManager.persist(statusOnGoing);
        entityManager.persist(statusRead);
    }
}
