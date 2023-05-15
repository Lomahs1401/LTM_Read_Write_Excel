package com.ltm.thiltm.model;

public class ExaminationRoom {
    private int STT;
    private int roomId;
    private String description;

    public ExaminationRoom() {
    }

    public ExaminationRoom(int STT, int roomId, String description) {
        this.STT = STT;
        this.roomId = roomId;
        this.description = description;
    }

    public int getSTT() {
        return STT;
    }

    public void setSTT(int STT) {
        this.STT = STT;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ExaminationRoom{" +
                "STT=" + STT +
                ", roomId=" + roomId +
                ", description='" + description + '\'' +
                '}';
    }
}
