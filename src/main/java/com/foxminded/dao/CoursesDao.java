package com.foxminded.dao;

import com.foxminded.enums.CourseName;
import com.foxminded.dao.factrory.DaoFactory;
import com.foxminded.domain.Course;
import com.foxminded.utility.SqlPartsCreator;
import com.foxminded.utility.StreamCloser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoursesDao {

    private DaoFactory daoFactory;

    public CoursesDao() {
        daoFactory = new DaoFactory();
    }

    public void saveCourses(List<Course> courses) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO courses(course_name, course_description) VALUES (?, ?)");) {

            for (var course : courses) {
                statement.setString(1, course.getName().toString());
                statement.setString(2, course.getDescription());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIdByName(String name) {
        ResultSet resultSet = null;
        int courseId;
        try (Connection connection = daoFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT course_id FROM courses WHERE course_name = ?")) {
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            resultSet.next();
            courseId = resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            StreamCloser.closeResultSet(resultSet);
        }

        return courseId;
    }

    public List<Course> getCoursesByIds(List<Integer> courseIds) {
        List<Course> courses = new ArrayList<>();
        ResultSet resultSet = null;
        String sql = "SELECT * FROM courses WHERE course_id " + SqlPartsCreator.createInPart(courseIds.size());
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < courseIds.size(); i++) {
                statement.setInt(i + 1, courseIds.get(i));
            }
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("course_id"));
                course.setName(CourseName.valueOf(resultSet.getString("course_name")));
                course.setDescription(resultSet.getString("course_description"));
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            StreamCloser.closeResultSet(resultSet);
        }

        return courses;
    }

}
