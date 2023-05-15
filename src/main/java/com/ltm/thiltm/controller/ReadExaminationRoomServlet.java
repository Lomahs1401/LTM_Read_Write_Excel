package com.ltm.thiltm.controller;

import com.ltm.thiltm.model.ExaminationRoom;
import com.ltm.thiltm.service.ExaminationRoomService;
import com.ltm.thiltm.service.impl.ExaminationRoomServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ReadExaminationRoomServlet", value = "/read-examination-room")
public class ReadExaminationRoomServlet extends HttpServlet {

    private ExaminationRoomService examinationRoomService;

    @Override
    public void init() throws ServletException {
        examinationRoomService = new ExaminationRoomServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis(); // Thời điểm bắt đầu đọc file
        List<ExaminationRoom> examinationRooms = examinationRoomService.getAllExaminationRooms();
        System.out.println("Danh sách phòng thi");
        for (ExaminationRoom examinationRoom : examinationRooms) {
            System.out.println(examinationRoom);
        }
        System.out.println("-----------------------------------------------");

        long endTime = System.currentTimeMillis(); // Thời điểm kết thúc đọc file
        long duration = endTime - startTime; // Thời gian đã đọc file (đơn vị: milliseconds)

        System.out.println("Thời gian đọc DB: " + duration + " milliseconds");

        resp.setContentType("text/html");
        resp.getWriter().write("Read file successfully! Time taken: " + duration + " milliseconds");
    }
}
