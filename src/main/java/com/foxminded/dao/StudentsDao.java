package com.foxminded.dao;

import com.foxminded.dao.factrory.DaoFactory;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.utility.SqlPartsCreator;
import com.foxminded.utility.StreamCloser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsDao {

    private DaoFactory daoFactory;

    public StudentsDao() {
        daoFactory = new DaoFactory();
    }

    public List<Integer> getGroupsIdByStudentNumber(int number) {
        List<Integer> groupIdList = new ArrayList<>();
        ResultSet resultSet = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT group_id FROM students GROUP BY group_id HAVING COUNT(*) <= ?")) {
            statement.setInt(1, number);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                groupIdList.add(resultSet.getInt("group_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            StreamCloser.closeResultSet(resultSet);
        }

        return groupIdList;
    }

    public void saveStudent(String firstName, String lastName, int groupId) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO students(group_id, first_name, last_name) VALUES (?, ?, ?)")) {
            statement.setInt(1, groupId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveStudents(List<Student> students) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO students(group_id, first_name, last_name) VALUES (?, ?, ?)");) {
            for (var student : students) {
                statement.setInt(1, student.getGroup().getId());
                statement.setString(2, student.getFirstName());
                statement.setString(3, student.getLastName());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Student, Integer> getStudentsByIds(List<Integer> studentIds) {
        ResultSet resultSet = null;
        Map<Student, Integer> studentsGroupIds = new HashMap<>();
        String sql = "SELECT * FROM students WHERE student_id " + SqlPartsCreator.createInPart(studentIds.size());
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < studentIds.size(); i++) {
                statement.setInt(i + 1, studentIds.get(i));
            }
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("student_id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                int groupId = resultSet.getInt("group_id");
                studentsGroupIds.put(student, groupId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            StreamCloser.closeResultSet(resultSet);
        }

        return studentsGroupIds;
    }

    public boolean deleteStudentById(int studentId) {
        int affectedRows;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM students WHERE student_id = ?")) {
            statement.setInt(1, studentId);
            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return affectedRows != 0;
    }

    public Map<Student, Integer> getStudentById(int studentId) {
        ResultSet resultSet = null;
        Map<Student, Integer> studentsGroupIds = new HashMap<>();
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM students WHERE student_id = ?")) {
            statement.setInt(1, studentId);
            resultSet = statement.executeQuery();

            resultSet.next();
            Student student = new Student();
            student.setId(resultSet.getInt("student_id"));
            student.setFirstName(resultSet.getString("first_name"));
            student.setLastName(resultSet.getString("last_name"));
            int groupId = resultSet.getInt("group_id");
            studentsGroupIds.put(student, groupId);

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

        return studentsGroupIds;
    }

}
