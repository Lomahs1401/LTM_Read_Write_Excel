package com.ltm.thiltm.service;

import com.ltm.thiltm.model.Teacher;

import java.util.List;

public interface TeacherService {
    List<Teacher> getAllTeachers();
    boolean createTeacher(Teacher teacher);
}
