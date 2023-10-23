package com.foxminded.dao;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GroupDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private final RowMapper<Group> groupRowMapper;
    private final ResultSetExtractor<Group> groupExtractor;

    @Autowired
    public GroupDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        groupRowMapper = (resultSet, rowNum) -> extractGroup(resultSet);

        groupExtractor = resultSet -> {
            if (!resultSet.next()) {
                throw new IllegalArgumentException(ErrorMessages.GROUP_WITH_THAT_ID_WAS_NOT_FOUND);
            }

            return extractGroup(resultSet);
        };
    }

    public void saveGroups(List<Group> groups) {
        jdbcTemplate.batchUpdate("INSERT INTO groups(group_name) VALUES (?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, groups.get(i).getName());
            }

            @Override
            public int getBatchSize() {
                return groups.size();
            }
        });
    }

    public List<Group> getGroupsByIds(List<Integer> groupIdList) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("ids", groupIdList);

        List<Group> groups = namedParamJdbcTemplate.query("SELECT * FROM groups where group_id IN (:ids)", parameterSource ,groupRowMapper);

        if (groups.size() != groupIdList.size()) {
            throw new IllegalArgumentException(ErrorMessages.SOME_GROUPS_WAS_NOT_FOUND);
        }

        return groups;
    }

    public Group getGroupById(int groupId) {
        return jdbcTemplate.query("SELECT * FROM groups where group_id = ?", groupExtractor, groupId);
    }

    public Group getGroupByName(String groupName) {
        return jdbcTemplate.query("SELECT * FROM groups where group_name = ?", groupExtractor, groupName);
    }

    public List<Group> getAllGroups() {
        return jdbcTemplate.query("SELECT * FROM groups", groupRowMapper);
    }

    public int getGroupsAmount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM groups", Integer.class);
    }

    public boolean deleteGroupById(int id) {
        return jdbcTemplate.update("DELETE FROM groups WHERE group_id = ?", id) != 0;
    }

    private Group extractGroup(ResultSet resultSet) {
        Group group = new Group();
        try {
            group.setName(resultSet.getString("group_name"));
            group.setId(resultSet.getInt("group_id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return group;
    }

}
