package com.foxminded.service.impl;

import com.foxminded.dao.GroupDao;
import com.foxminded.domain.Group;
import com.foxminded.dto.GroupDTO;
import com.foxminded.mappers.GroupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupsServiceImplTest {

    @InjectMocks
    private GroupsServiceImpl groupsService;
    @Mock
    private GroupDao groupDao;

    private GroupMapper groupMapper;

    @Test
    void testGetAllGroups_Success() {
        groupMapper = GroupMapper.INSTANCE;
        Group group1 = new Group();
        group1.setId(1);
        group1.setName("name1");

        Group group2 = new Group();
        group2.setId(2);
        group2.setName("name2");

        when(groupDao.getAllGroups()).thenReturn(List.of(group1, group2));
        assertEquals(List.of(group1, group2), groupsService.getAllGroups());
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