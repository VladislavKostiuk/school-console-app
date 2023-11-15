package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.repository.StudentRepository;
import com.foxminded.domain.Course;
import com.foxminded.domain.Student;
import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.StudentDTO;
import com.foxminded.mappers.CourseMapper;
import com.foxminded.mappers.GroupMapper;
import com.foxminded.mappers.StudentMapper;
import com.foxminded.service.StudentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentsServiceImpl implements StudentsService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final GroupMapper groupMapper;
    private final CourseMapper courseMapper;
    private final Logger logger;

    @Autowired
    public StudentsServiceImpl(StudentRepository studentRepository,
                               StudentMapper studentMapper,
                               GroupMapper groupMapper,
                               CourseMapper courseMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.groupMapper = groupMapper;
        this.courseMapper = courseMapper;
        logger = LoggerFactory.getLogger(StudentsServiceImpl.class);
    }

    @Override
    public List<GroupDTO> getGroupsByStudentAmount(int number) {
        logger.info("Getting group ids by amount of students {}", number);
        return studentRepository.findGroupsByStudentAmount(number).stream().map(groupMapper::mapToGroupDTO).toList();
    }

    @Override
    public void saveStudent(String firstName, String lastName, GroupDTO groupDTO) {
        logger.info("Saving student with first name {} and last name {}", firstName, lastName);
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setGroup(groupMapper.mapToGroup(groupDTO));
        studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(int id) {
        logger.info("Deleting student by id {}", id);
        studentRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public StudentDTO getStudentById(int id) {
        logger.info("Getting student by id {}", id);
        return studentMapper.mapToStudentDTO(studentRepository.findById(Long.valueOf(id)).orElseThrow(
                () -> new IllegalArgumentException(String.format(ErrorMessages.STUDENT_DOES_NOT_EXIST, id))
        ));
    }

    @Override
    public List<StudentDTO> getStudentsByCourse(CourseDTO courseDTO) {
        logger.info("Getting student by course {}", courseDTO.name());
        Course course = courseMapper.mapToCourse(courseDTO);
        return studentRepository.findStudentsByCoursesContaining(course).stream().map(studentMapper::mapToStudentDTO).toList();
    }

    @Override
    public void addStudentToCourse(StudentDTO studentDTO, CourseDTO courseDTO) {
        logger.info("Adding student {} to course {}", studentDTO, courseDTO.name());
        Course course = courseMapper.mapToCourse(courseDTO);
        Student student = studentMapper.mapToStudent(studentDTO);
        student.getCourses().add(course);
        studentRepository.save(student);
    }

    @Override
    public void deleteStudentFromCourse(StudentDTO studentDTO, CourseDTO courseDTO) {
        logger.info("Deleting student {} from course {}", studentDTO, courseDTO.name());
        Course course = courseMapper.mapToCourse(courseDTO);
        Student student = studentMapper.mapToStudent(studentDTO);
        student.getCourses().remove(course);
        studentRepository.save(student);
    }
}
