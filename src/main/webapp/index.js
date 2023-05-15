const readExcel = (event) => {
    event.preventDefault();
    var fileInput = document.getElementById('fileInput');
    var file = fileInput.files[0];
    var formData = new FormData();
    var fileSelected = false; // Biến cờ để kiểm tra người dùng đã chọn file hay chưa

    if (file) {
        fileSelected = true; // Đánh dấu là đã chọn file
        formData.append("file", file);
    }

    if (!fileSelected) {
        alert("Bạn chưa chọn file");
        return false; // Dừng thực hiện hàm
    }

    var fileForm = document.getElementById("fileForm");
    var url = fileForm.getAttribute("action");
    var method = fileForm.getAttribute("method");

    // Gửi request đến Servlet để xử lý đọc file Excel
    $.ajax({
        url: url,
        type: method,
        data: formData,
        cache: false,
        processData: false,
        contentType: false,
        success: function (response) {
            // Xử lý kết quả trả về từ Servlet
            alert(response);
        }
    });
}

const readTeacher = () => {
    // Gửi request đến Servlet để xử lý đọc file Excel
    $.ajax({
        url: '/read-teacher',
        type: 'POST',
        cache: false,
        processData: false,
        contentType: false,
        success: function (response) {
            alert(response);
        }
    });
}

const readExaminationRoom = () => {
    // Gửi request đến Servlet để xử lý đọc file Excel
    $.ajax({
        url: '/read-examination-room',
        type: 'POST',
        cache: false,
        processData: false,
        contentType: false,
        success: function (response) {
            alert(response);
        }
    });
}

const writeExcel = () => {
    // Gửi request đến Servlet để xử lý việc xuất file Excel
    $.ajax({
        url: "/write-excel",
        type: "POST",
        success: function (response) {
            // Xử lý kết quả trả về từ Servlet
            alert(response);
        }
    });
}

const shuffleData = () => {
    // Gửi request đến Servlet để xử lý việc xáo trộn dữ liệu
    $.ajax({
        url: "/shuffle-data",
        type: "POST",
        success: function (response) {
            // Xử lý kết quả trả về từ Servlet
            alert(response);
        }
    });
}