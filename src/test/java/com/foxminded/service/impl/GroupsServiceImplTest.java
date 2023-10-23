package com.foxminded.service.impl;

import com.foxminded.dao.GroupDao;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupsServiceImplTest {

    @InjectMocks
    GroupsServiceImpl groupsService;
    @Mock
    GroupDao groupDao;

    @Test
    void testGetGroupsByIds_Success() {
        Group group1 = new Group();
        group1.setId(1);
        group1.setName("name1");

        Group group2 = new Group();
        group2.setId(2);
        group2.setName("name2");

        List<Group> expectedList = List.of(group1, group2);
        doReturn(expectedList).when(groupDao).getGroupsByIds(List.of(1, 2));
        List<Group> actualList = groupDao.getGroupsByIds(List.of(1, 2));

        assertEquals(expectedList, actualList);
        verify(groupDao, times(1)).getGroupsByIds(List.of(1, 2));
    }

    @Test
    void testSetGroupToStudents_Success() {
        Student student = new Student();
        student.setId(1);

        Group group = new Group();
        group.setId(1);

        Map<Student, Integer> studentsGroupIds = new HashMap<>();
        studentsGroupIds.put(student, 1);

        when(groupDao.getGroupById(1)).thenReturn(group);

        assertEquals(List.of(student), groupsService.setGroupToStudents(studentsGroupIds));
        verify(groupDao, times(1)).getGroupById(1);
    }

    @Test
    void testGetAllGroupNames_Success() {
        Group group1 = new Group();
        group1.setId(1);
        group1.setName("name1");

        Group group2 = new Group();
        group2.setId(2);
        group2.setName("name2");

        when(groupDao.getAllGroups()).thenReturn(List.of(group1, group2));
        assertEquals(List.of("name1", "name2"), groupsService.getAllGroupNames());
        verify(groupDao, times(1)).getAllGroups();
    }

    @Test
    void testGetGroupByName_Success() {
        Group group = new Group();
        group.setId(1);
        group.setName("someName");
        when(groupDao.getGroupByName("someName")).thenReturn(group);

        assertEquals(group, groupsService.getGroupByName("someName"));
        verify(groupDao).getGroupByName("someName");
    }

    @Test
    void saveGroups_Success() {
        List<Group> groupList = List.of(new Group(), new Group());

        groupsService.saveGroups(groupList);
        verify(groupDao, times(1)).saveGroups(groupList);
    }

    @Test
    void testIsEmpty_Success() {
        doReturn(0).when(groupDao).getGroupsAmount();
        assertTrue(groupsService.isEmpty());
        verify(groupDao, times(1)).getGroupsAmount();

        doReturn(3).when(groupDao).getGroupsAmount();
        assertFalse(groupsService.isEmpty());
        verify(groupDao, times(2)).getGroupsAmount();
    }

}