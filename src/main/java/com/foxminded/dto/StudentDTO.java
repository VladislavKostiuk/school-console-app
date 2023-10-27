package com.foxminded.dto;

import java.util.List;

public record StudentDTO (
        int id,
        int groupId,
        String firstName,
        String lastName
) {}
