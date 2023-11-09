package com.foxminded.dao;

import com.foxminded.AbstractPostgreSQLTestContainer;
import com.foxminded.domain.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GroupDaoTest extends AbstractPostgreSQLTestContainer {

    private GroupDao groupDao;
    private Group testGroup1;
    private Group testGroup2;
    private Group testGroup3;
    @BeforeEach
    void init() {
        groupDao = new GroupDao(entityManager);

        testGroup1 = new Group();
        testGroup1.setId(1);
        testGroup1.setName("gr1");

        testGroup2 = new Group();
        testGroup2.setId(2);
        testGroup2.setName("gr2");

        testGroup3 = new Group();
        testGroup3.setId(3);
        testGroup3.setName("gr3");
    }

    @Test
    void testGetAllGroups_Success() {
        List<Group> expectedGroups = List.of(testGroup1, testGroup2, testGroup3);
        assertEquals(expectedGroups, groupDao.getAllGroups());
    }

    @Test
    void testSaveGroups_Success() {
        Group group4 = new Group();
        group4.setName("gr4");

        Group group5 = new Group();
        group5.setName("gr5");

        groupDao.saveGroups(List.of(group4, group5));
        assertEquals(5, groupDao.getGroupsAmount());
    }

    @Test
    void testGetGroupByName() {
        assertEquals(testGroup1, groupDao.getGroupByName("gr1"));
    }

    @Test
    void testGetGroupsAmount_Success() {
        assertEquals(3, groupDao.getGroupsAmount());
    }

}