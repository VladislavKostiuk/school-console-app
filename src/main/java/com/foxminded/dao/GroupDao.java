package com.foxminded.dao;

import com.foxminded.dao.factrory.DaoFactory;
import com.foxminded.domain.Group;
import com.foxminded.utility.SqlPartsCreator;
import com.foxminded.utility.StreamCloser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDao {

    private DaoFactory daoFactory;

    public GroupDao() {
        daoFactory = new DaoFactory();
    }

    public void saveGroups(List<Group> groups) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO groups(group_name) VALUES (?)")) {
            for (var group : groups) {
                statement.setString(1, group.getName());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Group> getGroupsByIds(List<Integer> groupIdList) {
        List<Group> groups = new ArrayList<>();
        ResultSet resultSet = null;
        String sql = "SELECT * FROM groups where group_id "
                + SqlPartsCreator.createInPart(groupIdList.size());
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < groupIdList.size(); i++) {
                statement.setInt(i + 1, groupIdList.get(i));
            }

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt("group_id"));
                group.setName(resultSet.getString("group_name"));
                groups.add(group);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            StreamCloser.closeResultSet(resultSet);
        }

        return groups;
    }

    public Group getGroupById(int groupId) {
        Group group;
        ResultSet resultSet = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM groups where group_id = ?")) {
            statement.setInt(1, groupId);
            resultSet = statement.executeQuery();
            resultSet.next();
            group = new Group();
            group.setId(resultSet.getInt("group_id"));
            group.setName(resultSet.getString("group_name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            StreamCloser.closeResultSet(resultSet);
        }

        return group;
    }

    public Group getGroupByName(String groupName) {
        Group group;
        ResultSet resultSet = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM groups where group_name = ?")) {
            statement.setString(1, groupName);
            resultSet = statement.executeQuery();
            resultSet.next();
            group = new Group();
            group.setId(resultSet.getInt("group_id"));
            group.setName(resultSet.getString("group_name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            StreamCloser.closeResultSet(resultSet);
        }

        return group;
    }

    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM groups");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt("group_id"));
                group.setName(resultSet.getString("group_name"));
                groups.add(group);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return groups;
    }

}
