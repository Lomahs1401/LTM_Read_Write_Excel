package com.ltm.thiltm.model;

import java.util.Date;

public class Teacher {
    private int STT;
    private int ID;
    private String fullName;
    private Date birthDate;
    private String workUnit;

    public Teacher() {
    }

    public Teacher(int STT, int ID, String fullName, Date birthDate, String workUnit) {
        this.STT = STT;
        this.ID = ID;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.workUnit = workUnit;
    }

    public int getSTT() {
        return STT;
    }

    public void setSTT(int STT) {
        this.STT = STT;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "STT=" + STT +
                ", ID=" + ID +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", workUnit='" + workUnit + '\'' +
                '}';
    }
}
