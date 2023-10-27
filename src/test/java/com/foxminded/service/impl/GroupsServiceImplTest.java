package com.foxminded.service.impl;

import com.foxminded.dao.GroupDao;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.mappers.GroupDTOMapper;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupsServiceImplTest {

    @InjectMocks
    private GroupsServiceImpl groupsService;
    @Mock
    private GroupDao groupDao;
    private GroupDTOMapper groupMapper;

    @BeforeEach
    void init() {
        groupMapper = new GroupDTOMapper();
    }

    @Test
    void testGetGroupsByIds_Success() {
        Group group1 = new Group();
        group1.setId(1);
        group1.setName("name1");

        Group group2 = new Group();
        group2.setId(2);
        group2.setName("name2");

        List<Group> groupList = List.of(group1, group2);
        doReturn(groupList).when(groupDao).getGroupsByIds(List.of(1, 2));
        List<GroupDTO> actualList = groupsService.getGroupsByIds(List.of(1, 2));
        List<GroupDTO> expectedList = groupList.stream().map(groupMapper::mapToGroupDTO).toList();

        assertEquals(expectedList, actualList);
        verify(groupDao, times(1)).getGroupsByIds(List.of(1, 2));
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
        GroupDTO actualGroup = groupMapper.mapToGroupDTO(group);

        assertEquals(actualGroup, groupsService.getGroupByName("someName"));
        verify(groupDao).getGroupByName("someName");
    }

}