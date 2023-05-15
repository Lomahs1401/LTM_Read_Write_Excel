package com.ltm.thiltm.service.impl;

import com.ltm.thiltm.model.ExaminationRoom;
import com.ltm.thiltm.repo.ExaminationRoomRepo;
import com.ltm.thiltm.service.ExaminationRoomService;

import java.util.List;

public class ExaminationRoomServiceImpl implements ExaminationRoomService {

    private final ExaminationRoomRepo examinationRoomRepo;

    public ExaminationRoomServiceImpl() {
        this.examinationRoomRepo = new ExaminationRoomRepo();
    }

    @Override
    public List<ExaminationRoom> getAllExaminationRooms() {
        return examinationRoomRepo.getAllExaminationRooms();
    }

    @Override
    public boolean createExaminationRoom(ExaminationRoom examinationRoom) {
        return examinationRoomRepo.createExaminationRoom(examinationRoom);
    }
}
