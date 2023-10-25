package com.foxminded.service;

import com.foxminded.domain.Group;
import com.foxminded.domain.Student;

import java.util.List;
import java.util.Map;

public interface GroupsService {

    List<Group> getGroupsByIds(List<Integer> ids);

    List<Student> setGroupToStudents(Map<Student, Integer> studentsGroupIds);

    List<String> getAllGroupNames();

    Group getGroupByName(String name);

}
