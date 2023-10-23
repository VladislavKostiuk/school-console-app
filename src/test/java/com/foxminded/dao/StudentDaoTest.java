package com.foxminded.dao;

import com.foxminded.AbstractPostgreSQLTestContainer;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class StudentDaoTest extends AbstractPostgreSQLTestContainer {

    private StudentDao studentDao;
    private Group group;

    @BeforeEach
    void init() {
        studentDao = new StudentDao(testDatasource);
        group = new Group();
        group.setId(1);
    }

    @Test
    void testSaveStudents_Success() {
        Student studentA = new Student();
        studentA.setId(6);
        studentA.setGroup(group);
        studentA.setFirstName("firstNameA");
        studentA.setLastName("lastNameA");

        Student studentB = new Student();
        studentB.setId(7);
        studentB.setGroup(group);
        studentB.setFirstName("firstNameB");
        studentB.setLastName("lastNameB");

        studentDao.saveStudents(List.of(studentA, studentB));

        Student fourth = convertMapToStudents(studentDao.getStudentById(6)).get(0);
        Student fifth = convertMapToStudents(studentDao.getStudentById(7)).get(0);

        studentA.setGroup(null);
        studentB.setGroup(null);
        studentDao.deleteStudentById(6);
        studentDao.deleteStudentById(7);

        assertEquals(studentA, fourth);
        assertEquals(studentB, fifth);
    }

    @Test
    void testGetGroupIdsByStudentNumber() {
        List<Integer> actual = studentDao.getGroupIdsByStudentNumber(1);
        Collections.sort(actual);

        assertEquals(List.of(1, 2, 3), actual);
    }

    @Test
    void testGetStudentById_Success() {
        Student expected = new Student();
        expected.setId(1);
        expected.setFirstName("firstName1");
        expected.setLastName("lastName1");

        Student actual = convertMapToStudents(studentDao.getStudentById(1)).get(0);


        assertEquals(expected, actual);
    }

    @Test
    void testGetStudentById_NoStudentWithSuchId() {
        assertThrows(IllegalArgumentException.class, () -> studentDao.getStudentById(100));
    }

    @Test
    void testSaveStudent_Success() {
        studentDao.saveStudent("firstName", "lastName", 1);

        Student actualStudent = convertMapToStudents(studentDao.getStudentById(4)).get(0);

        studentDao.deleteStudentById(4);

        assertEquals(4, actualStudent.getId());
        assertEquals("firstName", actualStudent.getFirstName());
        assertEquals("lastName", actualStudent.getLastName());
    }

    @Test
    void testGetStudentsByIds_Success() {
        Student student1 = new Student();
        student1.setId(1);
        student1.setFirstName("firstName1");
        student1.setLastName("lastName1");

        Student student2 = new Student();
        student2.setId(2);
        student2.setFirstName("firstName2");
        student2.setLastName("lastName2");

        assertEquals(new HashSet<>(List.of(student1, student2)),
                new HashSet<>(convertMapToStudents(studentDao.getStudentsByIds(List.of(1, 2)))));
    }

    @Test
    void testGetStudentsByIds_NoStudentsWithSuchIds() {
        assertThrows(IllegalArgumentException.class, () -> studentDao.getStudentsByIds(List.of(100, 200)));
    }

    @Test
    void testDeleteStudentById_Success() {
        studentDao.saveStudent("firstName", "lastName", 1);
        int beforeDelete = studentDao.getStudentsAmount();
        studentDao.deleteStudentById(5);
        int afterDelete = studentDao.getStudentsAmount();
        assertNotEquals(beforeDelete, afterDelete);
    }

    @Test
    void testGetStudentsAmount_Success() {
        assertEquals(3, studentDao.getStudentsAmount());
    }

    private List<Student> convertMapToStudents(Map<Student, Integer> mappedStudents) {
        List<Student> students = new ArrayList<>();

        for (var entry : mappedStudents.entrySet()) {
            students.add(entry.getKey());
        }

        return students;
    }

}