package com.foxminded.dao;

import com.foxminded.dao.factrory.DaoFactory;
import com.foxminded.domain.Course;
import com.foxminded.domain.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentsCoursesDao {
    private DaoFactory daoFactory;

    public StudentsCoursesDao() {
        daoFactory = new DaoFactory();
    }
    public void addSomeStudentsCourses(List<Student> students) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO students_courses(student_id, course_id) VALUES (?, ?)")) {
            for (var student : students) {
                List<Course> studentCourses = student.getCourses();
                for (var course : studentCourses) {
                    statement.setInt(1, student.getId());
                    statement.setInt(2, course.getId());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getStudentsIdByCourseId(int courseId) {
        ResultSet resultSet = null;
        List<Integer> studentIds = new ArrayList<>();
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT student_id FROM students_courses WHERE course_id = ?")) {
            statement.setInt(1, courseId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                studentIds.add(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return studentIds;
    }

    public boolean deleteStudentCoursesByStudentId(int studentId) {
        int affectedRows;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM students_courses WHERE student_id = ?")) {
            statement.setInt(1, studentId);
            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return affectedRows != 0;
    }

    public List<Integer> getCourseIdsByStudentId(int studentId) {
        ResultSet resultSet = null;
        List<Integer> courseIds = new ArrayList<>();
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT course_id FROM students_courses WHERE student_id = ?")) {
            statement.setInt(1, studentId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                courseIds.add(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return courseIds;
    }

    public void addStudentToCourse(int studentId, int courseId) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO students_courses VALUES (?, ?)")) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteStudentFromCourse(int studentId, int courseId) {
        int rowsAffected;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?")) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rowsAffected != 0;
    }
}
