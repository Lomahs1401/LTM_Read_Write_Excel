package com.ltm.thiltm.repo;

import com.ltm.thiltm.config.DBConnection;
import com.ltm.thiltm.model.ExaminationRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExaminationRoomRepo {
    public static final String GET_ALL_EXAMINATION_ROOMS = "SELECT * FROM examination_rooms;";
    public static final String INSERT_NEW_EXAMINATION_ROOM = "INSERT INTO examination_rooms(stt, id, description) VALUES (?, ?, ?);";

    public List<ExaminationRoom> getAllExaminationRooms() {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<ExaminationRoom> examinationRooms = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(GET_ALL_EXAMINATION_ROOMS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int stt = resultSet.getInt("stt");
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                examinationRooms.add(new ExaminationRoom(stt, id, description));
            }
            return examinationRooms;
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

    public boolean createExaminationRoom(ExaminationRoom examinationRoom) {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT_NEW_EXAMINATION_ROOM);
            preparedStatement.setInt(1, examinationRoom.getSTT());
            preparedStatement.setInt(2, examinationRoom.getRoomId());
            preparedStatement.setString(3, examinationRoom.getDescription());
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
