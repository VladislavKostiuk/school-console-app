package com.foxminded.dto.mappers;

import com.foxminded.domain.Group;
import com.foxminded.dto.GroupDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupDTOMapperTest {

    private GroupDTOMapper groupMapper;
    private Group testGroup;
    private GroupDTO testGroupDTO;

    @BeforeEach
    void init() {
        groupMapper = new GroupDTOMapper();

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