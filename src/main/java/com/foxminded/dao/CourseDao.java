package com.foxminded.dao;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.enums.CourseName;
import com.foxminded.domain.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private final ResultSetExtractor<Course> courseExtractor;

    @Autowired
    public CourseDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        courseExtractor = resultSet -> {
            if (!resultSet.next()) {
                throw new IllegalArgumentException(ErrorMessages.COURSE_WITH_THAT_ID_WAS_NOT_FOUND);
            }

            Course course = new Course();
            course.setId(resultSet.getInt("course_id"));
            course.setName(CourseName.valueOf(resultSet.getString("course_name")));
            course.setDescription(resultSet.getString("course_description"));
            return course;
        };
    }

    public void saveCourses(List<Course> courses) {
        jdbcTemplate.batchUpdate("INSERT INTO courses(course_name, course_description) VALUES (?, ?)",
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
        return jdbcTemplate.query("SELECT * FROM courses WHERE course_id = ?", courseExtractor, id);
    }

    public Optional<Integer> getIdByName(String name) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT course_id FROM courses " +
                "WHERE course_name = ?", Integer.class, name));
    }

    public List<Course> getCoursesByIds(List<Integer> courseIds) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("ids", courseIds);
        List<Course> courses = namedParamJdbcTemplate.query("SELECT * FROM courses WHERE course_id IN (:ids)", parameterSource,(resultSet, rowNum) -> {
            Course course = new Course();
            course.setId(resultSet.getInt("course_id"));
            course.setName(CourseName.valueOf(resultSet.getString("course_name")));
            course.setDescription(resultSet.getString("course_description"));
            return course;
        });

        if (courses.size() != courseIds.size()) {
            throw new IllegalArgumentException(ErrorMessages.SOME_COURSES_WAS_NOT_FOUND);
        }

        return courses;
    }

    public int getCoursesAmount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM courses", Integer.class);
    }

}
