package com.ltm.thiltm.service;

import com.ltm.thiltm.model.ExaminationRoom;

import java.util.List;

public interface ExaminationRoomService {
    List<ExaminationRoom> getAllExaminationRooms();
    boolean createExaminationRoom(ExaminationRoom examinationRoom);
}
