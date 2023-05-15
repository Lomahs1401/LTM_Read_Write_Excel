package com.ltm.thiltm.controller;

import com.ltm.thiltm.model.Teacher;
import com.ltm.thiltm.service.TeacherService;
import com.ltm.thiltm.service.impl.TeacherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ReadTeacherServlet", value = "/read-teacher")
public class ReadTeacherServlet extends HttpServlet {

    private TeacherService teacherService;

    @Override
    public void init() throws ServletException {
        teacherService = new TeacherServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis(); // Thời điểm bắt đầu đọc file
        List<Teacher> teachers = teacherService.getAllTeachers();
        System.out.println("Danh sách cán bộ");
        for (Teacher teacher : teachers) {
            System.out.println(teacher);
        }
        System.out.println("-----------------------------------------------");

        long endTime = System.currentTimeMillis(); // Thời điểm kết thúc đọc file
        long duration = endTime - startTime; // Thời gian đã đọc file (đơn vị: milliseconds)

        System.out.println("Thời gian đọc DB: " + duration + " milliseconds");

        resp.setContentType("text/html");
        resp.getWriter().write("Read file successfully! Time taken: " + duration + " milliseconds");
    }
}
