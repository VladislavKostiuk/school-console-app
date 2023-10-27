package com.foxminded.service.impl;

import com.foxminded.dao.CourseDao;
import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.mappers.CourseDTOMapper;
import com.foxminded.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class CoursesServiceImpl implements CoursesService {

    private final CourseDao courseDao;
    private final CourseDTOMapper courseMapper;

    @Autowired
    public CoursesServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
        courseMapper = new CourseDTOMapper();
    }

    @Override
    public CourseDTO getCourseByName(String name) {
        return courseMapper.mapToCourseDTO(courseDao.getCourseByName(name));
    }

    @Override
    public List<CourseDTO> getCoursesByIds(List<Integer> ids) {
        return courseDao.getCoursesByIds(ids)
                .stream()
                .map(courseMapper::mapToCourseDTO)
                .collect(toList());
    }

}
