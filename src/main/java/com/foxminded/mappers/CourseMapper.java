package com.foxminded.mappers;

import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDTO mapToCourseDTO(Course course);
    Course mapToCourse(CourseDTO courseDTO);

}
