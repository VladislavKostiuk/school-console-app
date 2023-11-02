package com.foxminded.dao;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.domain.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private RowMapper<Group> groupRowMapper;
    private ResultSetExtractor<Group> groupExtractor;
    private final Logger logger;

    @Autowired
    public GroupDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        logger = LoggerFactory.getLogger(GroupDao.class);

        initGroupRowMapper();
        initGroupExtractor();
    }

    public void saveGroups(List<Group> groups) {
        String sql = "INSERT INTO groups(group_name) VALUES (?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
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
        String sql = "SELECT * FROM groups where group_id IN (:ids)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("ids", groupIdList);

        List<Group> groups = namedParamJdbcTemplate.query(sql, parameterSource ,groupRowMapper);

        if (groups.size() != groupIdList.size()) {
            throw new IllegalArgumentException(ErrorMessages.SOME_GROUPS_WAS_NOT_FOUND);
        }

        return groups;
    }

    public Group getGroupById(int groupId) {
        String sql = "SELECT * FROM groups where group_id = ?";
        return jdbcTemplate.query(sql, groupExtractor, groupId);
    }

    public Group getGroupByName(String groupName) {
        String sql = "SELECT * FROM groups where group_name = ?";
        return jdbcTemplate.query(sql, groupExtractor, groupName);
    }

    public List<Group> getAllGroups() {
        String sql = "SELECT * FROM groups";
        return jdbcTemplate.query(sql, groupRowMapper);
    }

    public int getGroupsAmount() {
        String sql = "SELECT COUNT(*) FROM groups";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public boolean deleteGroupById(int id) {
        String sql = "DELETE FROM groups WHERE group_id = ?";
        return jdbcTemplate.update(sql, id) != 0;
    }

    private void initGroupExtractor() {
        groupExtractor = resultSet -> {
            if (!resultSet.next()) {
                throw new IllegalArgumentException(ErrorMessages.GROUP_WITH_THAT_ID_WAS_NOT_FOUND);
            }

            return extractGroup(resultSet);
        };
    }

    private void initGroupRowMapper() {
        groupRowMapper = (resultSet, rowNum) -> extractGroup(resultSet);
    }

    private Group extractGroup(ResultSet resultSet) {
        Group group = new Group();
        try {
            group.setName(resultSet.getString("group_name"));
            group.setId(resultSet.getInt("group_id"));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return group;
    }

}
