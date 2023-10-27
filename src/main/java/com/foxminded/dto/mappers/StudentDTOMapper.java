package com.foxminded.dto.mappers;

import com.foxminded.domain.Course;
import com.foxminded.domain.Student;
import com.foxminded.dto.StudentDTO;
import com.foxminded.enums.CourseName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class StudentDTOMapper {
    public List<StudentDTO> mapToStudentDTOList(Map<Student, Integer> studentGroupId) {
        List<StudentDTO> studentDTOList = new ArrayList<>();

        for (var entry : studentGroupId.entrySet()) {
            Student student = entry.getKey();
            int groupId = entry.getValue();
            studentDTOList.add(new StudentDTO(
                    student.getId(),
                    groupId,
                    student.getFirstName(),
                    student.getLastName()
            ));
        }

        return studentDTOList;
    }
}
