package com.ltm.thiltm.service.impl;

import com.ltm.thiltm.model.Teacher;
import com.ltm.thiltm.repo.TeacherRepo;
import com.ltm.thiltm.service.TeacherService;

import java.util.List;

public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepo teacherRepo;

    public TeacherServiceImpl() {
        this.teacherRepo = new TeacherRepo();
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepo.getAllTeachers();
    }

    @Override
    public boolean createTeacher(Teacher teacher) {
        return teacherRepo.createTeacher(teacher);
    }
}
