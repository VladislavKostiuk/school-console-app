package com.foxminded.service.impl;

import com.foxminded.mappers.CourseMapperImpl;
import com.foxminded.mappers.GroupMapperImpl;
import com.foxminded.repository.GroupRepository;
import com.foxminded.domain.Group;
import com.foxminded.dto.GroupDTO;
import com.foxminded.mappers.GroupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {
        GroupMapperImpl.class
})
class GroupsServiceImplTest {

    private GroupsServiceImpl groupsService;
    @Mock
    private GroupRepository groupRepository;

    @Autowired
    private GroupMapper groupMapper;

    @BeforeEach
    void init() {
        groupsService = new GroupsServiceImpl(groupRepository, groupMapper);
    }

    @Test
    void testGetAllGroups_Success() {
        Group group1 = new Group();
        group1.setId(1);
        group1.setName("name1");

        Group group2 = new Group();
        group2.setId(2);
        group2.setName("name2");

        when(groupRepository.findAll()).thenReturn(List.of(group1, group2));
        assertEquals(List.of(group1, group2), groupsService.getAllGroups());
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void testGetGroupByName_Success() {
        Group group = new Group();
        group.setId(1);
        group.setName("someName");
        when(groupRepository.findByName("someName")).thenReturn(Optional.of(group));
        GroupDTO actualGroup = groupMapper.mapToGroupDTO(group);

        assertEquals(actualGroup, groupsService.getGroupByName("someName"));
        verify(groupRepository).findByName("someName");
    }

    @Test
    void testGetGroupByName_GroupWithThatNameDoesNotExist() {
        when(groupRepository.findByName("some name")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> groupsService.getGroupByName("some name"));
    }

}