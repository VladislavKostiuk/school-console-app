package com.foxminded.mappers;

import com.foxminded.domain.Student;
import com.foxminded.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//@Mapper(uses = {GroupMapper.class, CourseMapper.class})
@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "student.group", target = "groupDTO")
    @Mapping(source = "student.courses", target = "coursesDTO")
    StudentDTO mapToStudentDTO(Student student);
    @Mapping(source = "studentDTO.groupDTO", target = "group")
    @Mapping(source = "studentDTO.coursesDTO", target = "courses")
    Student mapToStudent(StudentDTO studentDTO);
}
