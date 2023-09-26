package com.foxminded.services.impl;

import com.foxminded.dao.GroupsDao;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.services.GroupsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupsServiceImpl implements GroupsService {

    private final GroupsDao groupsDao;

    public GroupsServiceImpl() {
        groupsDao = new GroupsDao();
    }

    @Override
    public List<Group> getGroupsByIds(List<Integer> ids) {
        return groupsDao.getGroupsByIds(ids);
    }

    @Override
    public List<Student> setGroupToStudents(Map<Student, Integer> studentsGroupIds) {
        for (var entry : studentsGroupIds.entrySet()) {
            Group group = groupsDao.getGroupById(entry.getValue());
            Student student = entry.getKey();
            student.setGroup(group);
        }

        return new ArrayList<>(studentsGroupIds.keySet());
    }

    @Override
    public List<String> getAllGroupNames() {
        return groupsDao.getAllGroups()
                .stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Group getGroupByName(String name) {
        return groupsDao.getGroupByName(name);
    }

    @Override
    public void saveGroups(List<Group> groups) {
        groupsDao.saveGroups(groups);
    }

}
