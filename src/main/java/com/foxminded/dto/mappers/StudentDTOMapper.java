package com.foxminded.dto.mappers;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.dto.StudentDTO;
import com.foxminded.enums.CourseName;

public class StudentDTOMapper {

    public StudentDTO mapToStudentDTO(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getGroup().getId(),
                student.getGroup().getName(),
                student.getFirstName(),
                student.getLastName(),
                student.getCourses().stream().map(Course::getName).map(CourseName::toString).toList()
        );
    }

    public Student mapToStudent(StudentDTO studentDTO) {
        Group group = new Group();
        group.setId(studentDTO.groupId());
        group.setName(studentDTO.groupName());

        Student student = new Student();
        student.setId(studentDTO.id());
        student.setFirstName(studentDTO.firstName());
        student.setLastName(studentDTO.lastName());
        student.setGroup(group);

        return student;
    }

}
