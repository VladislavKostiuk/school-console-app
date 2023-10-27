package com.foxminded.dto.mappers;

import com.foxminded.domain.Group;
import com.foxminded.dto.GroupDTO;

public class GroupDTOMapper {
    public GroupDTO mapToGroupDTO(Group group) {
        return new GroupDTO (
                group.getId(),
                group.getName()
        );
    }
}
