package com.foxminded.dto.mappers;

import com.foxminded.domain.Group;
import com.foxminded.dto.GroupDTO;
import com.foxminded.mappers.GroupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


class GroupMapperTest {

    private GroupMapper groupMapper;
    private Group testGroup;
    private GroupDTO testGroupDTO;

    @BeforeEach
    void init() {
        groupMapper = GroupMapper.INSTANCE;
        int id = 1;
        String name = "group name";

        testGroup = new Group();
        testGroup.setId(id);
        testGroup.setName(name);

        testGroupDTO = new GroupDTO(
                id,
                name
        );
    }

    @Test
    void testMapToGroupDTO_Success() {
        assertEquals(testGroupDTO, groupMapper.mapToGroupDTO(testGroup));
    }

    @Test
    void testMapToGroup() {
        assertEquals(testGroup, groupMapper.mapToGroup(testGroupDTO));
    }

}