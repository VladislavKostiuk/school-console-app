package com.foxminded.dao;

import com.foxminded.enums.CourseName;
import com.foxminded.domain.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class CourseDao {
    @PersistenceContext
    private EntityManager entityManager;
    private final Logger logger;

    public CourseDao() {
        logger = LoggerFactory.getLogger(CourseDao.class);
    }

    public CourseDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        logger = LoggerFactory.getLogger(CourseDao.class);
    }

    public void saveCourses(List<Course> courses) {
        courses.forEach(entityManager::persist);
    }

    public Course getCourseByName(CourseName courseName) {
        String query = "SELECT c FROM Course c WHERE c.name = :name";
        return entityManager.createQuery(query, Course.class)
                .setParameter("name", courseName)
                .getSingleResult();
    }

    public int getCoursesAmount() {
        String query = "SELECT COUNT(*) FROM Course";
        return entityManager.createQuery(query, Long.class).getSingleResult().intValue();
    }

}
