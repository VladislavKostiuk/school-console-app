package com.foxminded.dao;

import com.foxminded.domain.Group;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class GroupDao {

    @PersistenceContext
    private EntityManager entityManager;

    public GroupDao() {
    }

    public GroupDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveGroups(List<Group> groups) {
        groups.forEach(entityManager::persist);
    }

    public Group getGroupByName(String groupName) {
        String query = "SELECT g FROM Group g where g.name = :name";
        return entityManager.createQuery(query, Group.class)
                .setParameter("name", groupName)
                .getSingleResult();
    }

    public List<Group> getAllGroups() {
        String query = "SELECT g FROM Group g";
        return entityManager.createQuery(query, Group.class).getResultList();
    }

    public int getGroupsAmount() {
        String query = "SELECT COUNT(*) FROM Group";
        return entityManager.createQuery(query, Long.class).getSingleResult().intValue();
    }

}
