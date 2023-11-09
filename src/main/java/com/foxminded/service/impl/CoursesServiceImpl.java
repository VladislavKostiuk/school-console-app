package com.foxminded.service.impl;

import com.foxminded.dao.CourseDao;
import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.mappers.CourseDTOMapper;
import com.foxminded.enums.CourseName;
import com.foxminded.service.CoursesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoursesServiceImpl implements CoursesService {

    private final CourseDao courseDao;
    private final CourseDTOMapper courseMapper;
    private final Logger logger;

    @Autowired
    public CoursesServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
        courseMapper = new CourseDTOMapper();
        logger = LoggerFactory.getLogger(CoursesServiceImpl.class);
    }

    @Override
    public CourseDTO getCourseByName(CourseName courseName) {
        logger.info("Getting course by name {}", courseName);
        return courseMapper.mapToCourseDTO(courseDao.getCourseByName(courseName));
    }

}
