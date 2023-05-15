package com.ltm.thiltm.repo;

import com.ltm.thiltm.config.DBConnection;
import com.ltm.thiltm.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepo {
    public static final String GET_ALL_TEACHERS = "SELECT * FROM teachers;";
    public static final String INSERT_NEW_TEACHER = "INSERT INTO teachers(stt, id, full_name, birth_date, work_unit) VALUES (?, ?, ?, ?, ?);";

    public List<Teacher> getAllTeachers() {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Teacher> teachers = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(GET_ALL_TEACHERS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int stt = resultSet.getInt("stt");
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("full_name");
                Date sqlDate = resultSet.getDate("birth_date");
                java.util.Date birthDate = new Date(sqlDate.getTime());
                String workUnit = resultSet.getString("work_unit");
                teachers.add(new Teacher(stt, id, fullName, birthDate, workUnit));
            }
            return teachers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                assert preparedStatement != null;
                assert resultSet != null;
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.close();
        }
    }

    public boolean createTeacher(Teacher teacher) {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT_NEW_TEACHER);
            preparedStatement.setInt(1, teacher.getSTT());
            preparedStatement.setInt(2, teacher.getID());
            preparedStatement.setString(3, teacher.getFullName());
            java.util.Date utilDate = teacher.getBirthDate();
            Date sqlDate = new Date(utilDate.getTime());
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.setString(5, teacher.getWorkUnit());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                assert preparedStatement != null;
                preparedStatement.close();
                DBConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
