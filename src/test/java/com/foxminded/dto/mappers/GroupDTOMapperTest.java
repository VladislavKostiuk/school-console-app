package com.foxminded.dto.mappers;

import com.foxminded.domain.Group;
import com.foxminded.dto.GroupDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupDTOMapperTest {

    private GroupDTOMapper groupMapper;

    @BeforeEach
    void init() {
        groupMapper = new GroupDTOMapper();
    }

    @Test
    void testMapToGroupDTO_Success() {
        int id = 1;
        String name = "group name";

        Group group = new Group();
        group.setId(id);
        group.setName(name);

        GroupDTO groupDTO = new GroupDTO(
                id,
                name
        );

        assertEquals(groupDTO, groupMapper.mapToGroupDTO(group));
    }

}