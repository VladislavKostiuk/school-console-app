package com.foxminded.dto;

import java.util.List;

public record StudentCourseDTO(
        int id,
        int group,
        String firstName,
        String lastName,
        List<String> courses
) {}
