package com.foxminded.service.impl;

import com.foxminded.dao.GroupDao;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.service.GroupsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupsServiceImpl implements GroupsService {

    private final GroupDao groupDao;

    public GroupsServiceImpl() {
        groupDao = new GroupDao();
    }

    @Override
    public List<Group> getGroupsByIds(List<Integer> ids) {
        return groupDao.getGroupsByIds(ids);
    }

    @Override
    public List<Student> setGroupToStudents(Map<Student, Integer> studentsGroupIds) {
        for (var entry : studentsGroupIds.entrySet()) {
            Group group = groupDao.getGroupById(entry.getValue());
            Student student = entry.getKey();
            student.setGroup(group);
        }

        return new ArrayList<>(studentsGroupIds.keySet());
    }

    @Override
    public List<String> getAllGroupNames() {
        return groupDao.getAllGroups()
                .stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Group getGroupByName(String name) {
        return groupDao.getGroupByName(name);
    }

    @Override
    public void saveGroups(List<Group> groups) {
        groupDao.saveGroups(groups);
    }

}
