package com.foxminded.service;

import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.StudentDTO;

import java.util.List;
import java.util.Map;

public interface GroupsService {

    List<Group> getAllGroups();

    GroupDTO getGroupByName(String name);

}
