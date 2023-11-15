package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.repository.GroupRepository;
import com.foxminded.domain.Group;
import com.foxminded.dto.GroupDTO;
import com.foxminded.mappers.GroupMapper;
import com.foxminded.service.GroupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroupsServiceImpl implements GroupsService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final Logger logger;

    @Autowired
    public GroupsServiceImpl(GroupRepository groupRepository,
                             GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        logger = LoggerFactory.getLogger(GroupsServiceImpl.class);
    }

    @Override
    public List<Group> getAllGroups() {
        logger.info("Getting all group names");
        return groupRepository.findAll();
    }

    @Override
    public GroupDTO getGroupByName(String name) {
        logger.info("Getting group by name {}", name);
        return groupMapper.mapToGroupDTO(groupRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException(String.format(ErrorMessages.GROUP_DOES_NOT_EXIST, name))
        ));
    }

}
