package com.foxminded.enums;

import com.foxminded.constants.ErrorMessages;

public enum CourseName {
    MATH,
    BIOLOGY,
    FINANCE,
    ART,
    ARCHITECTURE,
    ENGINEERING,
    SCIENCE,
    MANAGEMENT,
    ECONOMICS,
    MEDICINE;

    public static CourseName fromStringValue(String name) {
        try {
            return CourseName.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(ErrorMessages.COURSE_DOES_NOT_EXIST, name));
        }
    }
}
