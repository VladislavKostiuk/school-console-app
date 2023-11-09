package com.foxminded.dto;

import java.util.List;

public record StudentDTO (

        int id,
        int groupId,
        String groupName,
        String firstName,
        String lastName,
        List<String> courses

) {}
