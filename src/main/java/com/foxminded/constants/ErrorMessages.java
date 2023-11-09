package com.foxminded.constants;

public final class ErrorMessages {

    public final static String NO_MORE_AVAILABLE_NAMES_FOR_COURSES = "There are no more available names for courses";
    public final static String NO_MORE_AVAILABLE_NAMES_FOR_STUDENTS = "There are no more available names for students";
    public final static String IS_NOT_A_NUMBER = "%s is not a number";
    public final static String COURSE_WAS_NOT_FOUND = "Course %s was not found";
    public final static String WRONG_STUDENT_NAME = "Student first or last names can't be null or empty";
    public final static String GROUP_DOES_NOT_EXIST = "Group %s does not exist";
    public final static String STUDENT_DOES_NOT_HAVE_THAT_COURSE = "That student doesn't have %s in his course list";
    public final static String STUDENT_ALREADY_HAS_THAT_COURSE = "That student already has %s in his course list";

    private ErrorMessages() {}

}
