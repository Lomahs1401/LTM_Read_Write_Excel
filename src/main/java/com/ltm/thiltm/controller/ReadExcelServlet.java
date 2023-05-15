package com.ltm.thiltm.controller;

import com.ltm.thiltm.model.ExaminationRoom;
import com.ltm.thiltm.model.Teacher;
import com.ltm.thiltm.service.ExaminationRoomService;
import com.ltm.thiltm.service.TeacherService;
import com.ltm.thiltm.service.impl.ExaminationRoomServiceImpl;
import com.ltm.thiltm.service.impl.TeacherServiceImpl;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

@WebServlet(name = "ReadExcelServlet", value = "/read-excel")
@MultipartConfig
public class ReadExcelServlet extends HttpServlet {

    private TeacherService teacherService;
    private ExaminationRoomService examinationRoomService;

    @Override
    public void init() throws ServletException {
        teacherService = new TeacherServiceImpl();
        examinationRoomService = new ExaminationRoomServiceImpl();
    }

    // First sheet
    public static final int COLUMN_FIRST_SHEET_INDEX_STT = 0;
    public static final int COLUMN_FIRST_SHEET_INDEX_ID = 1;
    public static final int COLUMN_FIRST_SHEET_INDEX_FULL_NAME = 2;
    public static final int COLUMN_FIRST_SHEET_INDEX_BIRTHDATE = 3;
    public static final int COLUMN_FIRST_SHEET_INDEX_WORK_UNIT = 4;

    // Second sheet
    public static final int COLUMN_SECOND_SHEET_INDEX_STT = 0;
    public static final int COLUMN_SECOND_SHEET_INDEX_ROOM_ID = 1;
    public static final int COLUMN_SECOND_SHEET_INDEX_DESCRIPTION = 2;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {// Đường dẫn tới file Excel
        long startTime = System.currentTimeMillis(); // Thời điểm bắt đầu đọc file
        Part filePart = request.getPart("file");
        InputStream inputStream = filePart.getInputStream();
        try {
            OPCPackage opcPackage = OPCPackage.open(inputStream);

            // Get workbook
            Workbook workbook = new XSSFWorkbook(opcPackage);

            // Get sheet
            Sheet staffSheet = workbook.getSheetAt(0);
            Sheet examinationRoomSheet = workbook.getSheetAt(1);

            // Get all rows
            Iterator<Row> iteratorStaff = staffSheet.iterator();
            Iterator<Row> iteratorExaminationRoom = examinationRoomSheet.iterator();

            // Xử lý các ô cell
            DataFormatter dataFormatter = new DataFormatter();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");



            // Read first sheet
            while (iteratorStaff.hasNext()) {
                Row nextRow = iteratorStaff.next();

                // Get all cells
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                // Read cells and set value for staff and examination room object
                Teacher teacher = new Teacher();

                while (cellIterator.hasNext()) {
                    //Read cell
                    Cell cell = cellIterator.next();
                    Object cellValue = getCellValue(cell);

                    // Set value for book object
                    if (nextRow.getRowNum() != 0) {
                        int columnIndex = cell.getColumnIndex();
                        switch (columnIndex) {
                            case COLUMN_FIRST_SHEET_INDEX_STT:
                                teacher.setSTT(BigDecimal.valueOf((double) cellValue).intValue());
                                break;
                            case COLUMN_FIRST_SHEET_INDEX_ID:
                                teacher.setID(BigDecimal.valueOf((double) cellValue).intValue());
                                break;
                            case COLUMN_FIRST_SHEET_INDEX_FULL_NAME:
                                teacher.setFullName(String.valueOf(cellValue));
                                break;
                            case COLUMN_FIRST_SHEET_INDEX_BIRTHDATE:
                                // Lấy giá trị ngày sinh từ ô cell
                                String dateString = dataFormatter.formatCellValue(cell);
                                Date dateOfBirth = null;
                                try {
                                    dateOfBirth = dateFormat.parse(dateString);
                                } catch (ParseException e) {
                                    // Nếu không thành công, thử chuyển đổi bằng định dạng khác
                                    SimpleDateFormat alternativeDateFormat = new SimpleDateFormat("dd-MMM-yy");
                                    try {
                                        dateOfBirth = alternativeDateFormat.parse(dateString);
                                    } catch (ParseException ex) {
                                        // Nếu không thành công, gán giá trị mặc định hoặc xử lý khác tùy theo yêu cầu
                                        dateOfBirth = new Date();
                                    }
                                }
                                teacher.setBirthDate(dateOfBirth);
                                break;
                            case COLUMN_FIRST_SHEET_INDEX_WORK_UNIT:
                                teacher.setWorkUnit(String.valueOf(cellValue));
                                break;
                            default:
                                break;
                        }
                    }
                }

                if (nextRow.getRowNum() != 0) {
                    teacherService.createTeacher(teacher);
                    System.out.println(teacher);
                }
            }

            System.out.println("---------------------------------------------------------");

            // Read second sheet
            while (iteratorExaminationRoom.hasNext()) {
                Row nextRow = iteratorExaminationRoom.next();

                // Get all cells
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                // Read cells and set value for staff and examination room object
                ExaminationRoom examinationRoom = new ExaminationRoom();

                while (cellIterator.hasNext()) {
                    //Read cell
                    Cell cell = cellIterator.next();
                    Object cellValue = getCellValue(cell);

                    // Set value for book object
                    if (nextRow.getRowNum() != 0) {
                        int columnIndex = cell.getColumnIndex();
                        switch (columnIndex) {
                            case COLUMN_SECOND_SHEET_INDEX_STT:
                                examinationRoom.setSTT(BigDecimal.valueOf((double) cellValue).intValue());
                                break;
                            case COLUMN_SECOND_SHEET_INDEX_ROOM_ID:
                                examinationRoom.setRoomId(BigDecimal.valueOf((double) cellValue).intValue());
                                break;
                            case COLUMN_SECOND_SHEET_INDEX_DESCRIPTION:
                                if (cellValue == null) {
                                    examinationRoom.setDescription("");
                                } else {
                                    examinationRoom.setDescription(String.valueOf(cellValue));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }

                if (nextRow.getRowNum() != 0) {
                    examinationRoomService.createExaminationRoom(examinationRoom);
                    System.out.println(examinationRoom);
                }
            }

            long endTime = System.currentTimeMillis(); // Thời điểm kết thúc đọc file
            long duration = endTime - startTime; // Thời gian đã đọc file (đơn vị: milliseconds)

            System.out.println("Thời gian đọc file: " + duration + " milliseconds");

            workbook.close();
            inputStream.close();

            response.setContentType("text/html");
            response.getWriter().write("Read file successfully! Time taken: " + duration + " milliseconds");
        } catch (InvalidFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    // Get cell value
    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;
        switch (cellType) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            default:
                break;
        }

        return cellValue;
    }
}
