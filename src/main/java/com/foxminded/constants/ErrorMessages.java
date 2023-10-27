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
    public final static String NO_COURSE_WITH_SUCH_NAME = "There is no course with such name in db";
    public final static String COURSE_WITH_THAT_PARAM_WAS_NOT_FOUND = "Course with that id or name was not found";
    public final static String SOME_COURSES_WAS_NOT_FOUND = "Some courses with that ids was not found";
    public final static String GROUP_WITH_THAT_ID_WAS_NOT_FOUND = "Group with that id was not found";
    public final static String SOME_GROUPS_WAS_NOT_FOUND = "Some groups with that ids were not found";
    public final static String STUDENT_WITH_THAT_ID_WAS_NOT_FOUND = "Students with that id were not found";
    public final static String STUDENT_ID_WAS_NOT_FOUND = "Student id with that course id was not found";
    public final static String COURSE_ID_WAS_NOT_FOUND = "Course id with that student id was not found";

    private ErrorMessages() {}

}
