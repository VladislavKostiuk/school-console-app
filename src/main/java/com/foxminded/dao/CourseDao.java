package com.foxminded.dao;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.enums.CourseName;
import com.foxminded.domain.Course;
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
public class CourseDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private ResultSetExtractor<Course> courseExtractor;
    private RowMapper<Course> courseRowMapper;
    private final Logger logger;

    @Autowired
    public CourseDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        logger = LoggerFactory.getLogger(CourseDao.class);

        initCourseExtractor();
        initCourseRowMapper();
    }

    public void saveCourses(List<Course> courses) {
        String sql = "INSERT INTO courses(course_name, course_description) VALUES (?, ?)";
        logger.info("Saving courses to db");
        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, courses.get(i).getName().toString());
                        ps.setString(2, courses.get(i).getDescription());
                    }

                    @Override
                    public int getBatchSize() {
                        return courses.size();
                    }
                });
    }

    public Course getCourseById(int id) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        logger.info("Getting course by id {} from db", id);
        Course course = jdbcTemplate.query(sql, courseExtractor, id);
        return course;
    }

    public Course getCourseByName(String name) {
        String sql = "SELECT * FROM courses WHERE course_name = ?";
        logger.info("Getting course by name {} from db", name);
        Course course = jdbcTemplate.query(sql, courseExtractor, name);
        return course;
    }

    public List<Course> getCoursesByIds(List<Integer> courseIds) {
        String sql = "SELECT * FROM courses WHERE course_id IN (:ids)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("ids", courseIds);
        List<Course> courses = namedParamJdbcTemplate.query(sql, parameterSource, courseRowMapper);
        logger.info("Getting courses by ids: {} from db", courseIds);

        if (courses.size() != courseIds.size()) {
            throw new IllegalArgumentException(ErrorMessages.SOME_COURSES_WAS_NOT_FOUND);
        }

        return courses;
    }

    public int getCoursesAmount() {
        String sql = "SELECT COUNT(*) FROM courses";
        logger.info("Getting courses amount from db");
        int amount = jdbcTemplate.queryForObject(sql, Integer.class);
        return amount;
    }

    private void initCourseExtractor() {
        courseExtractor = resultSet -> {
            if (!resultSet.next()) {
                throw new IllegalArgumentException(ErrorMessages.COURSE_WITH_THAT_PARAM_WAS_NOT_FOUND);
            }

            return mapCourse(resultSet);
        };
    }

    private void initCourseRowMapper() {
        courseRowMapper = (resultSet, rowNum) -> mapCourse(resultSet);
    }

    private Course mapCourse(ResultSet resultSet) {
        Course course = new Course();
        try {
            course.setId(resultSet.getInt("course_id"));
            course.setName(CourseName.valueOf(resultSet.getString("course_name")));
            course.setDescription(resultSet.getString("course_description"));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return course;
    }

}
