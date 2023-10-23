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
    @BeforeEach
    void init() {
        groupDao = new GroupDao(testDatasource);
    }

    @Test
    void testGetAllGroups_Success() {
        Group firstExpected = new Group();
        firstExpected.setId(1);
        firstExpected.setName("gr1");

        Group secondExpected = new Group();
        secondExpected.setId(2);
        secondExpected.setName("gr2");

        Group thirdExpected = new Group();
        thirdExpected.setId(3);
        thirdExpected.setName("gr3");

        assertEquals(List.of(firstExpected, secondExpected, thirdExpected), groupDao.getAllGroups());
    }

    @Test
    void testGetGroupsAmount_Success() {
        int actualAmount = groupDao.getGroupsAmount();
        assertEquals(3, actualAmount);
    }

    @Test
    void testSaveGroups_Success() {
        Group groupA = new Group();
        groupA.setId(4);
        groupA.setName("grA");

        Group groupB = new Group();
        groupB.setId(5);
        groupB.setName("grB");

        groupDao.saveGroups(List.of(groupA, groupB));

        Group fourth = groupDao.getGroupById(4);
        Group fifth = groupDao.getGroupById(5);

        groupDao.deleteGroupById(4);
        groupDao.deleteGroupById(5);

        assertEquals(groupA, fourth);
        assertEquals(groupB, fifth);
    }

    @Test
    void testGetGroupsByIds_Success() {
        Group firstExpected = new Group();
        firstExpected.setId(1);
        firstExpected.setName("gr1");

        Group secondExpected = new Group();
        secondExpected.setId(2);
        secondExpected.setName("gr2");

        assertEquals(List.of(firstExpected, secondExpected), groupDao.getGroupsByIds(List.of(1, 2)));
    }

    @Test
    void testGetGroupsByIds_NoGroupsWithSuchIds() {
        assertThrows(IllegalArgumentException.class, () -> groupDao.getGroupsByIds(List.of(1, 100, 200)));
    }

    @Test
    void testGetGroupById_Success() {
        Group expected = new Group();
        expected.setId(1);
        expected.setName("gr1");

        assertEquals(expected, groupDao.getGroupById(1));
    }

    @Test
    void testGetGroupById_NoGroupWithSuchId() {
        assertThrows(IllegalArgumentException.class, () -> groupDao.getGroupById(100));
    }

    @Test
    void testGetGroupByName_Success() {
        Group expected = new Group();
        expected.setId(1);
        expected.setName("gr1");

        assertEquals(expected, groupDao.getGroupByName("gr1"));
    }

    @Test
    void testGetGroupByName_NoGroupWithSuchName() {
        assertThrows(IllegalArgumentException.class, () -> groupDao.getGroupByName(UUID.randomUUID().toString()));
    }

    @Test
    void testDeleteGroupById_Success() {
        Group group = new Group();
        group.setId(6);
        group.setName("gr6");

        groupDao.saveGroups(List.of(group));
        int beforeDelete = groupDao.getGroupsAmount();
        groupDao.deleteGroupById(6);
        int afterDelete = groupDao.getGroupsAmount();
        assertNotEquals(beforeDelete, afterDelete);
    }

}