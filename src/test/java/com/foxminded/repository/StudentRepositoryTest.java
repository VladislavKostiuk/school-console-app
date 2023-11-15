package com.foxminded.repository;

import com.foxminded.AbstractDaoTest;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class StudentRepositoryTest extends AbstractDaoTest {

    @Autowired
    private StudentRepository studentRepository;
    private Group testGroup;

    @BeforeEach
    void init() {
        testGroup = new Group();
        testGroup.setId(1);
        testGroup.setName("gr1");
    }
    @Test
    void testFindGroupsByStudentAmount_Success() {
        assertEquals(List.of(testGroup), studentRepository.findGroupsByStudentAmount(1));
    }

}