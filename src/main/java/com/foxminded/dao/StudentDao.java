package com.foxminded.dao;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
@Transactional
public class StudentDao {

    @PersistenceContext
    private EntityManager entityManager;
    private final Logger logger;

    public StudentDao() {
        logger = LoggerFactory.getLogger(StudentDao.class);
    }

    public StudentDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        logger = LoggerFactory.getLogger(StudentDao.class);
    }

    public void saveStudents(List<Student> students) {
        students.forEach(entityManager::persist);
    }

    public List<Group> getGroupsByStudentAmount(int number) {
        String query = "SELECT s.group FROM Student s GROUP BY s.group HAVING COUNT(*) <= :amount";
        return entityManager.createQuery(query, Group.class)
                .setParameter("amount", number)
                .getResultList();
    }

    public void saveStudent(Student student) {
        entityManager.persist(student);
    }

    public boolean deleteStudentById(int studentId) {
        String query = "DELETE FROM Student s WHERE s.id = :id";
        int rowsDeleted = entityManager.createQuery(query).setParameter("id", studentId).executeUpdate();
        return rowsDeleted != 0;
    }

    public Student getStudentById(int studentId) {
        return entityManager.find(Student.class, studentId);
    }

    public int getStudentsAmount() {
        String query = "SELECT COUNT(*) FROM Student";
        return entityManager.createQuery(query, Long.class).getSingleResult().intValue();
    }

    public List<Student> getStudentsByCourse(Course course) {
        String query = "SELECT s FROM Student s";
        List<Student> allStudents = entityManager.createQuery(query, Student.class).getResultList();
        List<Student> result = new ArrayList<>();
        for (var student : allStudents) {
            if (student.getCourses().contains(course)) {
                result.add(student);
            }
        }

        return result;
    }

    public void addStudentToCourse(Student student, Course course) {
        int id = student.getId();
        Student aStudent = entityManager.find(Student.class, id);
        aStudent.getCourses().add(course);
        entityManager.flush();
    }

    public boolean deleteStudentFromCourse(Student student, Course course) {
        int id = student.getId();
        Student aStudent = entityManager.find(Student.class, id);
        aStudent.getCourses().remove(course);
        entityManager.flush();
        return !aStudent.getCourses().contains(course);
    }

}
