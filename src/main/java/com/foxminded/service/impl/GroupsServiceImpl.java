package com.foxminded.service.impl;

import com.foxminded.dao.GroupDao;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.StudentDTO;
import com.foxminded.dto.mappers.GroupDTOMapper;
import com.foxminded.dto.mappers.StudentDTOMapper;
import com.foxminded.service.GroupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class GroupsServiceImpl implements GroupsService {

    private final GroupDao groupDao;
    private final GroupDTOMapper groupMapper;
    private final Logger logger;

    @Autowired
    public GroupsServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
        groupMapper = new GroupDTOMapper();
        logger = LoggerFactory.getLogger(GroupsServiceImpl.class);
    }

    @Override
    public List<GroupDTO> getGroupsByIds(List<Integer> ids) {
        logger.info("Getting groups by ids: {}", ids);
        return groupDao.getGroupsByIds(ids)
                .stream()
                .map(groupMapper::mapToGroupDTO)
                .collect(toList());
    }

    @Override
    public List<String> getAllGroupNames() {
        logger.info("Getting all group names");
        return groupDao.getAllGroups()
                .stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }

    @Override
    public GroupDTO getGroupByName(String name) {
        logger.info("Getting group by name {}", name);
        return groupMapper.mapToGroupDTO(groupDao.getGroupByName(name));
    }

}
