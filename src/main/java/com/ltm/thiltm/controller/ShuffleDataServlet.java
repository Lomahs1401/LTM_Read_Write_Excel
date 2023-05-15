package com.ltm.thiltm.controller;

import com.ltm.thiltm.model.ExaminationRoom;
import com.ltm.thiltm.model.Teacher;
import com.ltm.thiltm.service.ExaminationRoomService;
import com.ltm.thiltm.service.TeacherService;
import com.ltm.thiltm.service.impl.ExaminationRoomServiceImpl;
import com.ltm.thiltm.service.impl.TeacherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "ShuffleDataServlet", value = "/shuffle-data")
public class ShuffleDataServlet extends HttpServlet {
    private TeacherService teacherService;
    private ExaminationRoomService examinationRoomService;

    @Override
    public void init() throws ServletException {
        teacherService = new TeacherServiceImpl();
        examinationRoomService = new ExaminationRoomServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Teacher> teachers = teacherService.getAllTeachers();
        List<ExaminationRoom> examinationRooms = examinationRoomService.getAllExaminationRooms();

        // Xáo trộn danh sách
        List<Teacher> shuffledTeachers = new ArrayList<>(teachers);
        Collections.shuffle(shuffledTeachers);

        // Tạo Map để lưu trạng thái phân công
        Map<Integer, List<Integer>> assignmentMap = new HashMap<>();

        // Xếp cán bộ coi thi vào các phòng thi
        int staffIndex = 0;
        for (ExaminationRoom room : examinationRooms) {
            List<Integer> assignedStaffs = new ArrayList<>();

            // Lấy 2 cán bộ cho mỗi phòng thi
            for (int i = 0; i < 2; i++) {
                Teacher teacher = shuffledTeachers.get(staffIndex);
                assignedStaffs.add(teacher.getID());
                staffIndex++;
            }

            assignmentMap.put(room.getRoomId(), assignedStaffs);
        }

        // In kết quả phân công
        for (Map.Entry<Integer, List<Integer>> entry : assignmentMap.entrySet()) {
            int roomId = entry.getKey();
            List<Integer> assignedStaffs = entry.getValue();
            System.out.println("Phòng thi " + roomId + ": " + assignedStaffs);
        }

        response.setContentType("text/html");
        response.getWriter().write("Shuffled successfully!");
    }
}
