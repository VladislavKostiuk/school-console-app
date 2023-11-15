package com.foxminded.mappers;

import com.foxminded.domain.Group;
import com.foxminded.dto.GroupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDTO mapToGroupDTO(Group group);
    Group mapToGroup(GroupDTO groupDTO);

}
