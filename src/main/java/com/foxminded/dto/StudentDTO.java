package com.foxminded.dto;

import java.util.List;

public record StudentDTO (

        int id,
        GroupDTO groupDTO,
        String firstName,
        String lastName,
        List<CourseDTO> coursesDTO

) {}
