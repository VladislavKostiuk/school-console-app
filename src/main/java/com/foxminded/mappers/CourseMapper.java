package com.foxminded.mappers;

import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseMapper {
    CourseMapper INSTANCE  = Mappers.getMapper(CourseMapper.class);

    CourseDTO mapToCourseDTO(Course course);
    Course mapToCourse(CourseDTO courseDTO);

}
