package com.foxminded.service.impl;

import com.foxminded.dao.GroupDao;
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

    private final GroupDao groupDao;
    private final GroupMapper groupMapper;
    private final Logger logger;

    @Autowired
    public GroupsServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
        this.groupMapper = GroupMapper.INSTANCE;
        logger = LoggerFactory.getLogger(GroupsServiceImpl.class);
    }

    @Override
    public List<Group> getAllGroups() {
        logger.info("Getting all group names");
        return groupDao.getAllGroups();
    }

    @Override
    public GroupDTO getGroupByName(String name) {
        logger.info("Getting group by name {}", name);
        return groupMapper.mapToGroupDTO(groupDao.getGroupByName(name));
    }

}
