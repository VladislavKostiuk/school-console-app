package com.foxminded.service.impl;

import com.foxminded.dao.CourseDao;
import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.mappers.CourseDTOMapper;
import com.foxminded.service.CoursesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
    public CourseDTO getCourseByName(String name) {
        logger.info("Getting course by name {} from {}", name, CourseDao.class);
        return courseMapper.mapToCourseDTO(courseDao.getCourseByName(name));
    }

    @Override
    public List<CourseDTO> getCoursesByIds(List<Integer> ids) {
        logger.info("Getting courses by ids {} from {}", ids, CourseDao.class);
        return courseDao.getCoursesByIds(ids)
                .stream()
                .map(courseMapper::mapToCourseDTO)
                .collect(toList());
    }

}
