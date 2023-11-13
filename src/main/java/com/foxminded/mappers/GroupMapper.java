package com.foxminded.mappers;

import com.foxminded.domain.Group;
import com.foxminded.dto.GroupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);
    GroupDTO mapToGroupDTO(Group group);
    Group mapToGroup(GroupDTO groupDTO);

}
