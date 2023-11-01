package com.foxminded.dao;

import com.foxminded.constants.ErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentsCourseDao {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger;

    @Autowired
    public StudentsCourseDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.logger = LoggerFactory.getLogger(StudentsCourseDao.class);
    }


    public void saveStudentsCourses(List<int[]> studentCourses) {
        logger.info("Saving students courses to db");
        jdbcTemplate.batchUpdate("INSERT INTO students_courses(student_id, course_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        int[] values = studentCourses.get(i);
                        ps.setInt(1, values[0]);
                        ps.setInt(2, values[1]);
                    }

                    @Override
                    public int getBatchSize() {
                        return studentCourses.size();
                    }
                });

    }

    public List<Integer> getStudentIdsByCourseId(int courseId) {
        logger.info("Getting student ids by course id {} from db", courseId);
        return jdbcTemplate.query("SELECT student_id FROM students_courses WHERE course_id = ?",
                resultSet -> {
                    if (!resultSet.isBeforeFirst()) {
                        throw new IllegalArgumentException(ErrorMessages.STUDENT_ID_WAS_NOT_FOUND);
                    }

                    return getIdListFromResultSet(resultSet);
                } , courseId);
    }

    public boolean deleteStudentCoursesByStudentId(int studentId) {
        logger.info("Deleting student courses by student id {} from db", studentId);
        return jdbcTemplate.update("DELETE FROM students_courses WHERE student_id = ?", studentId) != 0;
    }

    public List<Integer> getCourseIdsByStudentId(int studentId) {
        logger.info("Getting course ids by student id {} from db", studentId);
        return jdbcTemplate.query("SELECT course_id FROM students_courses WHERE student_id = ?",
                resultSet -> {
                    if (!resultSet.isBeforeFirst()) {
                        throw new IllegalArgumentException(ErrorMessages.COURSE_ID_WAS_NOT_FOUND);
                    }

                    return getIdListFromResultSet(resultSet);
                }, studentId);
    }

    public void addStudentToCourse(int studentId, int courseId) {
        logger.info("Saving student id {} and course id {} to db", studentId, courseId);
        jdbcTemplate.update("INSERT INTO students_courses VALUES (?, ?)", studentId, courseId);
    }

    public boolean deleteStudentFromCourse(int studentId, int courseId) {
        logger.info("Deleting student id {} and course id {} from db", studentId, courseId);
        return jdbcTemplate.update("DELETE FROM students_courses WHERE student_id = ? AND course_id = ?",
                studentId, courseId) != 0;
    }

    public int getStudentCoursesAmount() {
        logger.info("Getting students courses amount from db");
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM students_courses", Integer.class);
    }

    private List<Integer> getIdListFromResultSet(ResultSet resultSet) {
        List<Integer> studentIds = new ArrayList<>();

        try {
            while (resultSet.next()) {
                studentIds.add(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return studentIds;
    }

}
