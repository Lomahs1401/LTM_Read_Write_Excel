package com.ltm.thiltm.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ltm.thiltm.model.ExaminationRoom;
import com.ltm.thiltm.model.Teacher;
import com.ltm.thiltm.service.ExaminationRoomService;
import com.ltm.thiltm.service.TeacherService;
import com.ltm.thiltm.service.impl.ExaminationRoomServiceImpl;
import com.ltm.thiltm.service.impl.TeacherServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet(name = "WriteExcelServlet", value = "/write-excel")
public class WriteExcelServlet extends HttpServlet {
    private TeacherService staffService;
    private ExaminationRoomService examinationRoomService;

    ArrayList<Integer> teacherList = new ArrayList<Integer>();
    ArrayList<Integer> roomList = new ArrayList<Integer>();
    ArrayList<Integer> assignedRoom = new ArrayList<Integer>();
    ArrayList<Integer> assignedTeacher = new ArrayList<Integer>();
    ArrayList<Integer> giamSatTeacher = new ArrayList<Integer>();

    @Override
    public void init() throws ServletException {
        staffService = new TeacherServiceImpl();
        examinationRoomService = new ExaminationRoomServiceImpl();
    }

    public static final int COLUMN_INDEX_STT = 0;
    public static final int COLUMN_INDEX_STAFF_ID = 1;
    public static final int COLUMN_INDEX_FULL_NAME = 2;
    public static final int COLUMN_INDEX_FIRST_STAFF = 3;
    public static final int COLUMN_INDEX_SECOND_STAFF = 4;
    public static final int COLUMN_INDEX_EXAMINATION_ROOM_ID = 5;
    private static CellStyle cellStyleFormatNumber = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String danhSachGiamThi = "C:\\Users\\Acer\\Desktop\\DANH_SACH_GIAM_THI.xlsx";
        String danhSachGiamSat = "C:\\Users\\Acer\\Desktop\\DANH_SACH_GIAM_SAT.xlsx";

        // Create Workbook
        Workbook workbook_giamthi = getWorkbook(danhSachGiamThi);
        Workbook workbook_giamsat = getWorkbook(danhSachGiamSat);

        // Create sheet_giamthi
        Sheet sheet_giamthi = workbook_giamthi.createSheet("Giám thị coi thi");
        Sheet sheet_giamsat = workbook_giamsat.createSheet("Cán bộ giám sát");

        int rowIndex_giamThi = 0;
        int rowIndex_giamSat = 0;

        // Write header
        writeHeaderGiamThi(sheet_giamthi, rowIndex_giamThi);
        writeHeaderGiamSat(sheet_giamsat, rowIndex_giamSat);

        // Get list data
        List<Teacher> teachers = staffService.getAllTeachers();
        List<ExaminationRoom> examinationRooms = examinationRoomService.getAllExaminationRooms();

        // Xáo trộn danh sách
        List<Teacher> shuffledTeachers = new ArrayList<>(teachers);
        Collections.shuffle(shuffledTeachers);

        // Tạo Map để lưu trạng thái phân công
        Map<Integer, List<Integer>> assignmentMap = new HashMap<>();
        List<Teacher> danhSachCanBoThua = shuffledTeachers;

        // Xếp cán bộ vào các phòng thi
        int teacherIndex = 0;
        rowIndex_giamThi += 2;
        for (ExaminationRoom room : examinationRooms) {
            List<Integer> assignedTeachers = new ArrayList<>();
            // Lấy 2 cán bộ cho mỗi phòng thi
            for (int i = 0; i < 2; i++) {
                Row row = sheet_giamthi.createRow(rowIndex_giamThi);
                Teacher teacher = shuffledTeachers.get(teacherIndex);
                writeCanBo(rowIndex_giamThi - 1, teacher, room.getRoomId(), row);
                assignedTeachers.add(teacher.getID());
                teacherIndex++;
                rowIndex_giamThi++;
                danhSachCanBoThua.remove(teacher);

            }

            assignmentMap.put(room.getRoomId(), assignedTeachers);
        }

        int numberOfStaffsPerRoom = 10; // Số cán bộ giám sát coi thi trong mỗi phòng
        int remainingStaffs = shuffledTeachers.size() - teacherIndex; // Số cán bộ còn lại sau khi bóc 974 phần tử

        int rowIndexGiamSat = 1;
        int sttGiamSat = 1;

        if (!danhSachCanBoThua.isEmpty()) {
            for (Teacher teacher : danhSachCanBoThua) {

                if (examinationRooms.isEmpty()) {
                    break; // Nếu danh sách phong thi đã hết, thoát khỏi vòng lặp
                }

                int remainingRooms = examinationRooms.size();

                ExaminationRoom phongThi =  examinationRooms.get(0);
                for (int i =0 ;i < Math.min(remainingRooms,numberOfStaffsPerRoom);i++) {
                    examinationRooms.remove(0);
                }

                int startRoom = phongThi.getRoomId(); // Tính phòng đầu tiên của range
                int endRoom = startRoom + Math.min(remainingRooms - 1, 9); // Tính phòng cuối cùng của range
                String supervisionRoomRange = "Từ " + startRoom + " đến " + endRoom;

                Row dataRow = sheet_giamsat.createRow(rowIndexGiamSat);
                dataRow.createCell(0).setCellValue(sttGiamSat); // STT
                dataRow.createCell(1).setCellValue(teacher.getID());
                dataRow.createCell(2).setCellValue(teacher.getFullName());
                dataRow.createCell(3).setCellValue(supervisionRoomRange); // Phòng thi được giám sát
                rowIndexGiamSat++;
                sttGiamSat++;
            }
        }

        // Auto resize column witdth
        int numberOfColumnGiamThi = sheet_giamthi.getRow(0).getPhysicalNumberOfCells();
        int numberOfColumnGiamSat = sheet_giamsat.getRow(0).getPhysicalNumberOfCells();

        autosizeColumn(sheet_giamthi, numberOfColumnGiamThi);
        autosizeColumn(sheet_giamsat, numberOfColumnGiamSat);

        // Create file excel
        createOutputFile(workbook_giamthi, danhSachGiamThi);
        createOutputFile(workbook_giamsat, danhSachGiamSat);

        workbook_giamthi.close();
        workbook_giamsat.close();

        System.out.println("Done!!!");

        response.setContentType("text/html");
        response.getWriter().write("Export file successfully!");


    }

    // Create workbook
    private static Workbook getWorkbook(String excelFilePath) throws IOException {
        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    // Write header with format
    private static void writeHeaderGiamThi(Sheet sheet, int rowIndex) {
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));

        // Create row
        Row row = sheet.createRow(rowIndex++);
        sheet.getRow(0).setHeightInPoints(30);

        CellStyle cellStyle = createStyleForHeader(sheet);

        Cell cell = row.createCell(COLUMN_INDEX_STT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");

        cell = row.createCell(COLUMN_INDEX_STAFF_ID);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Mã GV");

        cell = row.createCell(COLUMN_INDEX_FULL_NAME);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Họ và tên");

        cell = row.createCell(COLUMN_INDEX_FIRST_STAFF);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("GIÁM THỊ");

        cell = row.createCell(COLUMN_INDEX_EXAMINATION_ROOM_ID);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Phòng thi");

        row = sheet.createRow(rowIndex++);
        sheet.getRow(1).setHeightInPoints(30);

        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Giám thị 1");

        cell = row.createCell(4);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Giám thị 2");
    }

    // Write header with format
    private static void writeHeaderGiamSat(Sheet sheet, int rowIndex) {
        // Create row
        Row row = sheet.createRow(rowIndex);
        sheet.getRow(rowIndex).setHeightInPoints(30);

        CellStyle cellStyle = createStyleForHeader(sheet);

        Cell cell = row.createCell(COLUMN_INDEX_STT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");

        cell = row.createCell(COLUMN_INDEX_STAFF_ID);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Mã GV");

        cell = row.createCell(COLUMN_INDEX_FULL_NAME);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Họ và tên");

        cell = row.createCell(COLUMN_INDEX_FIRST_STAFF);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Phòng thi được giám sát");
    }

    // Write data
    private static void writeCanBo(int rowIndex, Teacher teacher, int roomId, Row row) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }

        Cell cell = row.createCell(COLUMN_INDEX_STT);
        cell.setCellValue(rowIndex);

        cell = row.createCell(COLUMN_INDEX_STAFF_ID);
        cell.setCellValue(teacher.getID());

        cell = row.createCell(COLUMN_INDEX_FULL_NAME);
        cell.setCellValue(teacher.getFullName());

        if (rowIndex % 2 == 0) {
            cell = row.createCell(COLUMN_INDEX_FIRST_STAFF);
            cell.setCellValue("X");

            cell = row.createCell(COLUMN_INDEX_SECOND_STAFF);
            cell.setCellValue("");
        } else {
            cell = row.createCell(COLUMN_INDEX_FIRST_STAFF);
            cell.setCellValue("");

            cell = row.createCell(COLUMN_INDEX_SECOND_STAFF);
            cell.setCellValue("X");
        }

        cell = row.createCell(COLUMN_INDEX_EXAMINATION_ROOM_ID);
        cell.setCellValue(roomId);
    }

    // Write data
    private static void writeGiamSat(int rowIndex, Teacher teacher, int roomId, Row row) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }

        Cell cell = row.createCell(COLUMN_INDEX_STT);
        cell.setCellValue(rowIndex);

        cell = row.createCell(COLUMN_INDEX_STAFF_ID);
        cell.setCellValue(teacher.getID());

        cell = row.createCell(COLUMN_INDEX_FULL_NAME);
        cell.setCellValue(teacher.getFullName());

        if (rowIndex % 2 == 0) {
            cell = row.createCell(COLUMN_INDEX_FIRST_STAFF);
            cell.setCellValue("X");

            cell = row.createCell(COLUMN_INDEX_SECOND_STAFF);
            cell.setCellValue("");
        } else {
            cell = row.createCell(COLUMN_INDEX_FIRST_STAFF);
            cell.setCellValue("");

            cell = row.createCell(COLUMN_INDEX_SECOND_STAFF);
            cell.setCellValue("X");
        }

        cell = row.createCell(COLUMN_INDEX_EXAMINATION_ROOM_ID);
        cell.setCellValue(roomId);
    }

    // Create CellStyle for header
    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

    // Auto resize column width
    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }

    // Create output file
    private static void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
        try (OutputStream os = Files.newOutputStream(Paths.get(excelFilePath))) {
            workbook.write(os);
        }
    }

//    public void xuLyGiamThi() {
//        ArrayList<Integer> teacherListTmp = new ArrayList<>(teacherList);
//        Random random = new Random();
//
//        assignedTeacher.clear();
//        assignedRoom.clear();
//        roomCount = 0;
//
//        while(roomCount < roomList.size() * 2) {
//            int index = random.nextInt(teacherListTmp.size());
//            int teacherId = teacherListTmp.get(index);
//            ArrayList<Integer> history = teacherHistory.get(teacherId);
//            int roomId = roomList.get(roomCount / 2);
//            if(!history.contains(roomId)) {
//                history.add(roomId);
//                teacherHistory.put(teacherId, history);
//                assignedTeacher.add(teacherId);
//                assignedRoom.add(roomId);
//                teacherListTmp.remove(index);
//                roomCount++;
//            }
//        }
//        xuLyGiamSat(teacherListTmp);
//    }
//
//    public void xuLyGiamSat(ArrayList<Integer> teacherListTmp) {
//        ArrayList<Integer> giamSatTeacherTmp = new ArrayList<>(teacherListTmp);
//        Random random = new Random();
//        giamSatTeacher.clear();
//
//        for(int i = 0; i < roomList.size() / roomsPerTeacher; i++) {
//            int index = random.nextInt(giamSatTeacherTmp.size());
//            int teacherId = giamSatTeacherTmp.get(index);
//            giamSatTeacher.add(teacherId);
//            giamSatTeacherTmp.remove(index);
//        }
//
//        if(roomList.size() % roomsPerTeacher != 0) {
//            int index = random.nextInt(giamSatTeacherTmp.size());
//            int teacherId = giamSatTeacherTmp.get(index);
//            giamSatTeacher.add(teacherId);
//            giamSatTeacherTmp.remove(index);
//        }
//    }
}







