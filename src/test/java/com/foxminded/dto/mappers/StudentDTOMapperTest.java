package com.foxminded.dto.mappers;

import com.foxminded.domain.Student;
import com.foxminded.dto.StudentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentDTOMapperTest {
    private StudentDTOMapper studentMapper;

    @BeforeEach
    void init() {
        studentMapper = new StudentDTOMapper();
    }

    @Test
    void testMapToStudentDTOList_Success() {
        int id = 1;
        int groupId = 1;
        String firstName = "first name";
        String lastName = "last name";

        Student student = new Student();
        student.setId(id);
        student.setFirstName(firstName);
        student.setLastName(lastName);

        Map<Student, Integer> studentMap = new HashMap<>();
        studentMap.put(student, groupId);

        List<StudentDTO> studentDTOList = List.of(
                new StudentDTO (
                id,
                groupId,
                firstName,
                lastName
        ));

        assertEquals(studentDTOList, studentMapper.mapToStudentDTOList(studentMap));
    }

}