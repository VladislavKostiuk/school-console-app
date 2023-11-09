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

    public Group mapToGroup(GroupDTO groupDTO) {
        Group group = new Group();
        group.setId(groupDTO.id());
        group.setName(groupDTO.name());

        return group;
    }

}
