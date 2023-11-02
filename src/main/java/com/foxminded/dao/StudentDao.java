package com.foxminded.dao;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.domain.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class StudentDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private ResultSetExtractor<Map<Student, Integer>> studentExtractor;
    private final Logger logger;

    @Autowired
    public StudentDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        logger = LoggerFactory.getLogger(StudentDao.class);

        initStudentExtractor();
    }

    public void saveStudents(List<Student> students) {
        jdbcTemplate.batchUpdate("INSERT INTO students(group_id, first_name, last_name) VALUES (?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, students.get(i).getGroup().getId());
                        ps.setString(2, students.get(i).getFirstName());
                        ps.setString(3, students.get(i).getLastName());
                    }

                    @Override
                    public int getBatchSize() {
                        return students.size();
                    }
                });
    }

    public List<Integer> getGroupIdsByStudentNumber(int number) {
        List<Integer> groupIdList = new ArrayList<>();
        jdbcTemplate.query("SELECT group_id FROM students GROUP BY group_id HAVING COUNT(*) <= ?", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                groupIdList.add(resultSet.getInt("group_id"));
            }
        }, number);

        return groupIdList;
    }

    public void saveStudent(String firstName, String lastName, int groupId) {
        jdbcTemplate.update("INSERT INTO students(group_id, first_name, last_name) VALUES (?, ?, ?)",
                groupId, firstName, lastName);
    }

    public Map<Student, Integer> getStudentsByIds(List<Integer> studentIds) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("ids", studentIds);
        return namedParamJdbcTemplate.query("SELECT * FROM students WHERE student_id IN (:ids)", parameterSource, studentExtractor);

    }

    public boolean deleteStudentById(int studentId) {
        return jdbcTemplate.update("DELETE FROM students WHERE student_id = ?", studentId) != 0;
    }

    public Map<Student, Integer> getStudentById(int studentId) {
        return jdbcTemplate.query("SELECT * FROM students WHERE student_id = ?", studentExtractor, studentId);
    }

    public int getStudentsAmount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM students", Integer.class);
    }

    private void initStudentExtractor() {
        studentExtractor = resultSet -> {
            if (!resultSet.isBeforeFirst()) {
                throw new IllegalArgumentException(ErrorMessages.STUDENT_WITH_THAT_ID_WAS_NOT_FOUND);
            }

            Map<Student, Integer> studentsGroupIds = new HashMap<>();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("student_id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                int groupId = resultSet.getInt("group_id");
                studentsGroupIds.put(student, groupId);
            }
            return studentsGroupIds;
        };
    }

}
