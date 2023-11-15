package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.domain.Course;
import com.foxminded.repository.CourseRepository;
import com.foxminded.dto.CourseDTO;
import com.foxminded.mappers.CourseMapper;
import com.foxminded.enums.CourseName;
import com.foxminded.service.CoursesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoursesServiceImpl implements CoursesService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final Logger logger;

    @Autowired
    public CoursesServiceImpl(CourseRepository courseRepository,
                              CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        logger = LoggerFactory.getLogger(CoursesServiceImpl.class);
    }

    @Override
    public CourseDTO getCourseByName(CourseName courseName) {
        logger.info("Getting course by name {}", courseName);
        Course course = courseRepository.findByName(courseName).orElseThrow(
                () -> new IllegalArgumentException(String.format(ErrorMessages.COURSE_DOES_NOT_EXIST, courseName))
        );
        return courseMapper.mapToCourseDTO(course);
    }

}
